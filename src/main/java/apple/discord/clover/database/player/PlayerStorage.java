package apple.discord.clover.database.player;

import apple.discord.clover.database.CloverDatabase;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.activity.query.QDPlaySession;
import apple.discord.clover.database.activity.run.DDungeonRun;
import apple.discord.clover.database.activity.run.DLevelupRun;
import apple.discord.clover.database.activity.run.DRaidRun;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.player.query.QDPlayer;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.discord.clover.wynncraft.stats.player.character.WynnPlayerCharacter;
import io.ebean.DB;
import io.ebean.Transaction;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerStorage {

    public static boolean save(DLoginQueue login, WynnPlayer currentValue) {
        DPlaySession lastSession = new QDPlaySession().where()
            .player.uuid.eq(currentValue.uuid)
            .orderBy().retrievedTime.desc()
            .setMaxRows(1)
            .findOne();
        if (lastSession != null && login.joinTime.equals(lastSession.retrievedTime)) {
            // no time has passed
            return true;
        }

        DPlayer playerInDB = new QDPlayer().where().uuid.eq(currentValue.uuid).findOne();
        if (playerInDB == null) {
            playerInDB = new DPlayer(currentValue.uuid, currentValue.username);
            playerInDB.insert();
        } else {
            playerInDB.setUsername(currentValue.username).save();
        }

        DPlaySession session = new DPlaySession(playerInDB, lastSession, login, currentValue);
        try (Transaction transaction = DB.beginTransaction()) {
            session.insert(transaction);
            for (Entry<UUID, WynnPlayerCharacter> dataChar : currentValue.characters.entrySet()) {
                DCharacter lastChar = lastSession == null ? null : lastSession.getCharacter(dataChar.getKey());
                DCharacter character = new DCharacter(dataChar.getKey(), session, dataChar.getValue(), lastChar);
                character.insert(transaction);
                try {
                    character.addRuns(dataChar.getValue(), lastSession);
                } catch (LastSessionInvalidException e) {
                    transaction.rollback();
                    if (lastChar == null) {
                        throw new RuntimeException("lastChar is null!!", e);
                    } else {
                        fixCorruptionDeleteSession(lastChar.getSession());
                    }
                    return false;
                }
                for (DRaidRun raid : character.raidRuns) {
                    raid.insert(transaction);
                }
                for (DLevelupRun level : character.levelRuns) {
                    level.insert(transaction);
                }
                for (DDungeonRun dungeon : character.dungeonRuns) {
                    dungeon.insert(transaction);
                }
            }
            transaction.commit();
        }
        return true;
    }

    private static void fixCorruptionDeleteSession(DPlaySession session) {
        CloverDatabase.get().logger().info("Fixing corruption of session= {}", session.id);
        try (Transaction transaction = DB.beginTransaction()) {
            session.getCharacters().forEach(character -> {
                character.getDungeonRuns().forEach(d -> d.delete(transaction));
                character.getLevelRuns().forEach(d -> d.delete(transaction));
                character.getRaidRuns().forEach(d -> d.delete(transaction));
                character.delete(transaction);
            });
            session.delete(transaction);
            transaction.commit();
        }

    }

    @Nullable
    public static String findPlayerName(UUID player) {
        QDPlayer a = QDPlayer.alias();
        return new QDPlayer().select(a.username)
            .where().uuid.eq(player)
            .findSingleAttribute();
    }

    @Nullable
    public static UUID findPlayerId(String player) {
        QDPlayer a = QDPlayer.alias();
        return new QDPlayer().select(a.uuid)
            .where().username.ieq(player)
            .findSingleAttribute();
    }

    @NotNull
    public static List<DPlayer> findLikePlayer(String inputPattern, int limit) {
        if (inputPattern.isBlank()) return Collections.emptyList();

        String escaped = CloverDatabase.DATABASE_PLATFORM.escapeLikeString(inputPattern);
        StringBuilder outPattern = new StringBuilder("%");

        Matcher matcher = Pattern.compile("\\w").matcher(escaped);

        if (!matcher.find()) return Collections.emptyList(); // no input query

        int start = 0;
        do {
            outPattern.append(escaped, start, start = matcher.end())
                .append("%");
        } while (matcher.find());
        return new QDPlayer()
            .where().username.ilike(outPattern.toString())
            .setMaxRows(limit)
            .findList();
    }

    public static DPlayer findPlayer(UUID uuid) {
        return new QDPlayer().where().uuid.eq(uuid).findOne();
    }
}
