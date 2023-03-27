package apple.discord.clover.service;

public class ServiceModuleConfig {

    private static ServiceModuleConfig instance;
    private boolean shouldEnable = true;

    public ServiceModuleConfig() {
        instance = this;
    }

    public static ServiceModuleConfig get() {
        return instance;
    }

    public boolean shouldEnable() {
        return shouldEnable;
    }
}
