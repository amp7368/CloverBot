package apple.discord.clover.database.player;

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
import java.util.Map.Entry;
import java.util.UUID;

public class PlayerStorage {

    public static void save(DLoginQueue login, WynnPlayer currentValue) {
        DPlaySession lastSession = new QDPlaySession().where().player.uuid.eq(currentValue.uuid).orderBy().joinTime.desc()
            .setMaxRows(1)
            .findOne();

        DPlayer playerInDB = new QDPlayer().where().uuid.eq(currentValue.uuid).findOne();
        if (playerInDB == null) {
            playerInDB = new DPlayer(currentValue.uuid, currentValue.username);
            playerInDB.insert();
        } else {
            playerInDB.setUsername(currentValue.username);
            playerInDB.save();
        }
        try (Transaction transaction = DB.beginTransaction()) {
            DPlaySession session = new DPlaySession(playerInDB, lastSession, login, currentValue);
            session.insert(transaction);

            for (Entry<UUID, WynnPlayerCharacter> dataChar : currentValue.characters.entrySet()) {
                DCharacter character = new DCharacter(dataChar.getKey(), session, dataChar.getValue(), lastSession);
                character.insert();

                character.addRuns(dataChar.getValue(), lastSession);
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
    }

}
