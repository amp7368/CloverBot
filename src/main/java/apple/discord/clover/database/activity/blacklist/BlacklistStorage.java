package apple.discord.clover.database.activity.blacklist;

import apple.discord.clover.database.activity.blacklist.query.QDBlacklist;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import io.ebean.DB;
import io.ebean.Transaction;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

public class BlacklistStorage {

    private static final TemporalAmount BLACKLIST_WAIT = Duration.of(1, ChronoUnit.DAYS);
    private static final int MAX_FAILURES = 15;

    public synchronized static void success(DLoginQueue login) {
        try (Transaction transaction = DB.beginTransaction()) {
            DBlacklist blacklist = query(login).usingTransaction(transaction).findOne();
            if (blacklist != null) {
                blacklist.incrementSuccess()
                    .setLogin(null)
                    .save(transaction);
            }
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
