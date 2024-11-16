package apple.discord.clover.database.queue.blacklist;

import apple.discord.clover.CloverBot;
import apple.discord.clover.database.queue.blacklist.query.QDBlacklist;
import apple.discord.clover.database.queue.partial.DLoginQueue;
import io.ebean.DB;
import io.ebean.Transaction;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlacklistStorage {

    private static final TemporalAmount BLACKLIST_WAIT = Duration.of(1, ChronoUnit.DAYS);
    private static final int MAX_FAILURES = 15;
    private static final Duration CLEANUP_LAST_FAILURE = Duration.of(7, ChronoUnit.DAYS);

    public static void load() {
        ScheduledExecutorService executor = CloverBot.get().executor();
        Runnable command = BlacklistStorage::cleanupBlacklist;
        long period = CLEANUP_LAST_FAILURE.getSeconds();
        executor.scheduleAtFixedRate(command, 0, period, TimeUnit.SECONDS);
    }

    public static void cleanupBlacklist() {
        Instant lastFailureCleanup = Instant.now().minus(CLEANUP_LAST_FAILURE);
        new QDBlacklist().where()
            .or()
            .success.gt(0)
            .lastFailure.before(Timestamp.from(lastFailureCleanup))
            .delete();
    }

    public synchronized static void success(DLoginQueue login) {
        try (Transaction transaction = DB.beginTransaction()) {
            DBlacklist blacklist = query(login).usingTransaction(transaction).findOne();
            if (blacklist == null) return;
            blacklist.incrementSuccess()
                .setLogin(null)
                .save(transaction);
            transaction.commit();
        }
    }

    public synchronized static void failure(DLoginQueue login) {
        try (Transaction transaction = DB.beginTransaction()) {
            DBlacklist blacklist = query(login).usingTransaction(transaction).findOne();
            if (blacklist != null) {
                blacklist.setLogin(login)
                    .incrementFailure()
                    .save(transaction);
            } else new DBlacklist(login).save(transaction);
            transaction.commit();
        }
    }

    public static QDBlacklist query(DLoginQueue login) {
        return new QDBlacklist().where().or().login.id.eq(login.id).username.eq(login.player).endOr();
    }

    public static Instant getLastAllowedFailure() {
        return Instant.now().minus(BLACKLIST_WAIT);
    }

    public static int getMaxFailures() {
        return MAX_FAILURES;
    }
}
