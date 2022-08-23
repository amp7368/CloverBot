package apple.discord.clover;

import apple.discord.clover.util.FileIOService;
import apple.utilities.database.ajd.AppleAJDInstImpl;

public class CloverConfig {
    private static AppleAJDInstImpl<CloverConfig> manager;
    private static CloverConfig instance;
    private String token;
    private long testingServerId;

    public static void load() {
        manager = new AppleAJDInstImpl<>(CloverBot.getBuildFile("cloverConfig.json"), CloverConfig.class, FileIOService.get());
        instance = manager.loadNowOrMake();
    }

    public static CloverConfig get() {
        return instance;
    }

    public String getToken() {
        return this.token;
    }

    public long getTestingServer() {
        return this.testingServerId;
    }
}
