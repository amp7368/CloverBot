package apple.discord.clover;

import apple.discord.clover.api.ApiConfig;
import apple.discord.clover.discord.DiscordConfig;
import apple.discord.clover.service.ServiceModuleConfig;
import apple.lib.modules.configs.data.config.init.AppleConfigInit;

public class CloverConfig extends AppleConfigInit {

    private static CloverConfig instance;
    protected DiscordConfig discord = new DiscordConfig();
    protected ServiceModuleConfig service = new ServiceModuleConfig();
    protected ApiConfig api = new ApiConfig();

    public CloverConfig() {
        instance = this;
    }

    public static CloverConfig get() {
        return instance;
    }

    public static DiscordConfig getDiscord() {
        return instance.discord;
    }

    public static ServiceModuleConfig getService() {
        return instance.service;
    }

    public static ApiConfig getApi() {
        return instance.api;
    }

    @Override
    public void onInitConfig() {
        service.onInitConfig();
    }
}
