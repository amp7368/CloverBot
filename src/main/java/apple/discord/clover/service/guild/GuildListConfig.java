package apple.discord.clover.service.guild;

import apple.discord.clover.service.base.DaemonServiceConfig;

public class GuildListConfig extends DaemonServiceConfig {

    private static GuildListConfig instance;

    public GuildListConfig() {
        instance = this;
    }

    public static GuildListConfig get() {
        return instance;
    }


}
