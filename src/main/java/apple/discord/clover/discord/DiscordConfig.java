package apple.discord.clover.discord;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordConfig {

    private static DiscordConfig instance;
    protected String token = "YourTokenHere";
    protected long reportsChannelId = 0;
    private transient TextChannel reportsChannel = null;

    public DiscordConfig() {
        instance = this;
    }

    public static DiscordConfig get() {
        return instance;
    }

    public void load() {
        reportsChannel = DiscordModule.dcf.jda().getChannelById(TextChannel.class, reportsChannelId);
        assert reportsChannel != null;
    }

    public String getToken() {
        return token;
    }

    public TextChannel getReportsChannel() {
        return reportsChannel;
    }
}
