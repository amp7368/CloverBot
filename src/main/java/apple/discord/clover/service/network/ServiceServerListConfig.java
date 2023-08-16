package apple.discord.clover.service.network;

import apple.discord.clover.service.base.DaemonServiceConfig;

public class ServiceServerListConfig extends DaemonServiceConfig {

    private static ServiceServerListConfig instance;

    public ServiceServerListConfig() {
        instance = this;
    }

    public static ServiceServerListConfig get() {
        return instance;
    }

}
