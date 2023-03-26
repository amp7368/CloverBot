package apple.discord.clover.service.network;

import apple.lib.modules.configs.data.config.AppleConfig;
import java.time.Duration;
import java.time.Instant;

public class ServiceServerListConfig {

    public static AppleConfig<ServiceServerListConfig> config;
    private Instant lastQuery = Instant.MIN;

    private static ServiceServerListConfig get() {
        return config.getInstance();
    }

    public static void updateLastQuery() {
        get().lastQuery = Instant.now();
        config.save();
    }

    public static void sleepIfLastQueryRecent(long inLastMillis) {
        Instant nextQuery = get().lastQuery.plusMillis(inLastMillis);
        Duration sleepTime = Duration.between(Instant.now(), nextQuery);
        if (sleepTime.isNegative()) return;
        try {
            Thread.sleep(sleepTime.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
