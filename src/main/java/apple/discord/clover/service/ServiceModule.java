package apple.discord.clover.service;

import apple.discord.clover.service.network.NetworkModule;
import apple.lib.modules.AppleModule;
import java.util.List;

public class ServiceModule extends AppleModule {

    @Override
    public List<AppleModule> getModules() {
        return List.of(new NetworkModule());
    }

    @Override
    public String getName() {
        return "Service";
    }
}
