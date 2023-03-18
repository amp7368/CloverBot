package apple.discord.clover.service;

import apple.discord.clover.service.network.ServiceServerList;
import apple.discord.clover.service.player.ServicePlayerStats;
import apple.lib.modules.AppleModule;

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
        new ServicePlayerStats();
        new ServiceServerList();
    }

    @Override
    public String getName() {
        return "Service";
    }
}
