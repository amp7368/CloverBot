package apple.discord.clover.service;

import apple.discord.clover.CloverConfig;
import apple.discord.clover.database.activity.blacklist.BlacklistStorage;
import apple.discord.clover.discord.DiscordBot;
import apple.discord.clover.service.guild.GuildListConfig;
import apple.discord.clover.service.guild.GuildListService;
import apple.discord.clover.service.network.ServiceServerList;
import apple.discord.clover.service.network.ServiceServerListConfig;
import apple.discord.clover.service.player.ServicePlayerStats;
import apple.discord.clover.service.status.CloverStatusService;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.data.config.AppleConfig.Builder;
import apple.lib.modules.configs.factory.AppleConfigLike;
import java.util.List;

public class ServiceModule extends AppleModule {

    private static ServiceModule instance;

    public ServiceModule() {
        instance = this;
    }

    public static ServiceModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        if (!CloverConfig.getService().shouldEnable()) return;
        DiscordBot.awaitReady(this::onReady);
        BlacklistStorage.load();
    }

    private void onReady() {
        CloverStatusService.load();
        new GuildListService();
        new ServicePlayerStats();
        new ServiceServerList();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        Builder<ServiceServerListConfig> serverList = configJson(ServiceServerListConfig.class, "ServerList.config")
            .asJson(WynncraftModule.gson());
        Builder<GuildListConfig> guildList = configJson(GuildListConfig.class, "GuildList.config")
            .asJson(WynncraftModule.gson());
        return List.of(serverList, guildList);
    }

    @Override
    public String getName() {
        return "Service";
    }
}
