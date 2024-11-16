package apple.discord.clover.database.queue.partial;

import apple.discord.clover.database.queue.blacklist.BlacklistStorage;
import apple.discord.clover.database.queue.blacklist.DBlacklist;
import apple.discord.clover.database.queue.blacklist.query.QDBlacklist;
import apple.discord.clover.database.queue.partial.query.QDLoginQueue;
import apple.discord.clover.service.ServiceModule;
import io.ebean.DB;
import io.ebean.Model;
import io.ebean.Transaction;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class LoginStorage {

    public static final int FIND_UPDATES_ROW_LIMIT = 100;
    private static final TemporalAmount ONLINE_TOO_LONG = Duration.ofHours(6);
    private static final int MIN_OFFLINE_COUNT_REQUIRED = 1;

    public static void queuePlayers(List<UUID> players, Instant requestedAt) {
        QDLoginQueue alias = QDLoginQueue.alias();
        new QDLoginQueue().asUpdate().set(alias.isOnline, false).update();
        try (Transaction transaction = DB.beginTransaction()) {
            for (UUID player : players) {
                int didUpdate = new QDLoginQueue()
                    .usingTransaction(transaction)
                    .where().player.eq(player.toString())
                    .asUpdate()
                    .set(alias.isOnline, true).update();
                if (didUpdate == 0)
                    new DLoginQueue(player, requestedAt).save(transaction);
            }
            transaction.commit();
        }

        new QDLoginQueue().where()
            .isOnline.eq(false)
            .asUpdate()
            .setRaw("offline = offline + 1")
            .setRaw("leave_time = now()").update();
    }

    public static Collection<DLoginQueue> findUpdates() {
        cleanBlacklist();
        List<DLoginQueue> updates = queryNextUpdates()
            .joinTime.before(Timestamp.from(getOnlineTooLong()))
            .orderBy().joinTime.asc()
            .setMaxRows(FIND_UPDATES_ROW_LIMIT).findList();
        if (updates.size() == FIND_UPDATES_ROW_LIMIT) return updates;
        updates.addAll(queryNextUpdates()
            .offline.ge(MIN_OFFLINE_COUNT_REQUIRED)
            .orderBy().offline.desc()
            .setMaxRows(FIND_UPDATES_ROW_LIMIT - updates.size())
            .findList());
        return new HashSet<>(updates);
    }

    private static void cleanBlacklist() {
        List<DBlacklist> failedInBlacklist = new QDBlacklist().where()
            .failure.ge(BlacklistStorage.getMaxFailures())
            .findList();
        List<UUID> loginIds = failedInBlacklist.stream()
            .map(DBlacklist::getLogin)
            .map(DLoginQueue::getId)
            .toList();
        failedInBlacklist.forEach(Model::delete);
        new QDLoginQueue().where().id.in(loginIds).delete();
    }

    private static Instant getOnlineTooLong() {
        return Instant.now().minus(ONLINE_TOO_LONG);
    }

    @NotNull
    private static QDLoginQueue queryNextUpdates() {
        Timestamp lastAllowedFailure = Timestamp.from(BlacklistStorage.getLastAllowedFailure());
        return new QDLoginQueue()
            .where()
            .or()
            .blacklist.isNull()
            .and()
            .blacklist.isNotNull()
            .blacklist.lastFailure.before(lastAllowedFailure)
            .endAnd().endOr();
    }

    public static void success(DLoginQueue login) {
        BlacklistStorage.success(login);
        login.delete();
    }

    public static void failure(DLoginQueue login) {
        ServiceModule.get().logger().info("Incrementing blacklist on %s".formatted(login.player));
        BlacklistStorage.failure(login);
    }
}
