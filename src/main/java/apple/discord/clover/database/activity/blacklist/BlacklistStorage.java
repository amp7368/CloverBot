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
        if (!query(login).exists()) new DBlacklist(login).save();
        query(login).asUpdate().setRaw("failure = failure + 1").update();
    }

    public static QDBlacklist query(DLoginQueue login) {
        return new QDBlacklist().where().login.id.eq(login.id);
    }

    public static boolean tryQueue(String player) {
        DBlacklist blacklist = new QDBlacklist().where().login.player.eq(player).findOne();
        if (blacklist == null) return true;
        Instant nextAllowed = blacklist.lastFailure.toInstant().plus(BLACKLIST_WAIT);
        return Instant.now().isAfter(nextAllowed);
    }

    public static Instant getLastAllowedFailure() {
        return Instant.now().minus(BLACKLIST_WAIT);
    }
}
