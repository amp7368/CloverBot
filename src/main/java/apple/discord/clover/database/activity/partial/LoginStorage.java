package apple.discord.clover.database.activity.partial;

import apple.discord.clover.database.activity.blacklist.BlacklistStorage;
import apple.discord.clover.database.activity.partial.query.QDLoginQueue;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class LoginStorage {

    public static final int FIND_UPDATES_ROW_LIMIT = 100;
    private static final TemporalAmount ONLINE_TOO_LONG = Duration.ofHours(6);

    public static void queuePlayers(List<String> players, Instant requestedAt) {
        QDLoginQueue alias = QDLoginQueue.alias();
        new QDLoginQueue().asUpdate().set(alias.isOnline, false).update();
        for (String player : players) {
            int didUpdate = new QDLoginQueue().where().player.eq(player).asUpdate().set(alias.isOnline, true).update();
            if (didUpdate == 0)
                new DLoginQueue(player, requestedAt).save();
        }
        new QDLoginQueue().where().isOnline.eq(false).asUpdate().setRaw("offline = offline + 1").setRaw("leave_time = now()").update();
    }

    public static List<DLoginQueue> findUpdates() {
        List<DLoginQueue> updates = queryNotBlacklist()
            .joinTime.before(Timestamp.from(getOnlineTooLong()))
            .orderBy().joinTime.asc()
            .setMaxRows(FIND_UPDATES_ROW_LIMIT).findList();
        if (updates.size() == FIND_UPDATES_ROW_LIMIT) return updates;
        updates.addAll(queryNotBlacklist()
            .orderBy().offline.desc()
            .setMaxRows(FIND_UPDATES_ROW_LIMIT - updates.size())
            .findList());
        return updates;
    }

    private static Instant getOnlineTooLong() {
        return Instant.now().minus(ONLINE_TOO_LONG);
    }

    @NotNull
    private static QDLoginQueue queryNotBlacklist() {
        Timestamp lastAllowedFailure = Timestamp.from(BlacklistStorage.getLastAllowedFailure());
        return new QDLoginQueue()
            .where()
            .and()
            .offline.gt(0)
            .or()
            .blacklist.isNull()
            .and()
            .blacklist.isNotNull()
            .blacklist.lastFailure.before(lastAllowedFailure)
            .endAnd().endOr().endAnd();
    }

    public static void success(DLoginQueue login) {
        login.delete();
        BlacklistStorage.success(login);
    }

    public static void failure(DLoginQueue login) {
        BlacklistStorage.failure(login);
    }
}
