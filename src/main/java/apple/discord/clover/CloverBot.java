package apple.discord.clover;

import apple.discord.clover.api.ApiModule;
import apple.discord.clover.database.CloverDatabase;
import apple.discord.clover.discord.DiscordModule;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.lib.modules.AppleModule;
import apple.lib.modules.ApplePlugin;
import apple.lib.modules.configs.factory.AppleConfigLike;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class CloverBot extends ApplePlugin {

    private static CloverBot instance;

    public static void main(String[] args) {
        instance = new CloverBot();
        instance.start();
    }

    public static CloverBot get() {
        return instance;
    }

    @Override
    public String getName() {
        return "CloverBot";
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(CloverConfig.class, "CloverConfig").setPretty());
    }

    @Override
    protected ScheduledExecutorService makeExecutor() {
        return new ScheduledThreadPoolExecutor(4);
    }

    @Override
    public List<AppleModule> createModules() {
        return List.of(new CloverDatabase(), new WynncraftModule(), new ApiModule(), new ServiceModule(), new DiscordModule());
    }
}
