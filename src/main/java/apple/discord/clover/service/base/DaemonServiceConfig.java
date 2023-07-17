package apple.discord.clover.service.base;

import apple.lib.modules.configs.data.config.init.AppleConfigInit;
import java.time.Duration;
import java.time.Instant;

public class DaemonServiceConfig extends AppleConfigInit {

    protected Instant lastQuery = Instant.MIN;

    public void sleepIfLastQueryRecent(long inLastMillis) {
        Instant nextQuery = lastQuery.plusMillis(inLastMillis);
        Duration sleepTime = Duration.between(Instant.now(), nextQuery);
        if (sleepTime.isNegative()) return;
        try {
            Thread.sleep(sleepTime.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateLastQuery() {
        lastQuery = Instant.now();
        save();
    }
}
