package apple.discord.clover.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordConfig {

    private static DiscordConfig instance;
    protected String token = "YourTokenHere";
    protected long reportsChannelId = 0;
    protected long logChannelId = 0;
    protected long devServerId = 603039156892860417L;
    private transient TextChannel reportsChannel;
    private transient TextChannel logChannel;

    public DiscordConfig() {
        instance = this;
    }

    public static DiscordConfig get() {
        return instance;
    }

    public void load() {
        reportsChannel = DiscordModule.dcf.jda().getTextChannelById(reportsChannelId);
        logChannel = DiscordModule.dcf.jda().getTextChannelById(logChannelId);
        assert reportsChannel != null;
        assert logChannel != null;
    }

    public String getToken() {
        return token;
    }

    public TextChannel getReportsChannel() {
        return reportsChannel;
    }

    public TextChannel getLogChannel() {
        return logChannel;
    }

    public boolean isDevServer(Guild guild) {
        if (guild == null) return false;
        return devServerId == guild.getIdLong();
    }
}
