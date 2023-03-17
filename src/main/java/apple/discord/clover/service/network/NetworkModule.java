package apple.discord.clover.service.network;

import apple.lib.modules.AppleModule;

public class NetworkModule extends AppleModule {

    private static NetworkModule instance;

    public NetworkModule() {
        instance = this;
    }

    public static NetworkModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        new ServerList();
    }

    @Override
    public String getName() {
        return "Network";
    }
}
