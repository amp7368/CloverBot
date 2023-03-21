package apple.discord.clover.database.activity.blacklist;

import apple.discord.clover.database.activity.blacklist.query.QDBlacklist;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

public class BlacklistStorage {

    private static final TemporalAmount BLACKLIST_WAIT = Duration.of(1, ChronoUnit.DAYS);

    public static void success(DLoginQueue login) {
        query(login).asUpdate().setRaw("success = success + 1").update();
    }

    public static void failure(DLoginQueue login) {
        if (query(login).exists()) {
            query(login).asUpdate()
                .setRaw("failure = failure + 1")
                .set(QDBlacklist.alias().login, login)
                .update();
        } else new DBlacklist(login).save();
    }

    public static QDBlacklist query(DLoginQueue login) {
        return new QDBlacklist().where().username.eq(login.player);
    }

    public static Instant getLastAllowedFailure() {
        return Instant.now().minus(BLACKLIST_WAIT);
    }
}
