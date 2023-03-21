package apple.discord.clover.service;

import apple.discord.clover.service.guild.GuildService;
import apple.discord.clover.service.network.ServiceServerList;
import apple.discord.clover.service.network.ServiceServerListConfig;
import apple.discord.clover.service.player.ServicePlayerStats;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
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
        GuildService.load();
        new ServicePlayerStats();
        new ServiceServerList();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        Builder<ServiceServerListConfig> serverList = configJson(ServiceServerListConfig.class, "ServerList.config")
            .asJson(WynncraftRatelimit.gson());
        ServiceServerListConfig.config = serverList.getConfig();
        return List.of(serverList);
    }

    @Override
    public String getName() {
        return "Service";
    }
}
