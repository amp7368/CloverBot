package apple.discord.clover.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordConfig {

    protected String token = "token";
    protected long systemChannelId = 882396439420993546L;
    protected long commandLogChannelId = 769737908293992509L;
    protected long devServerId = 603039156892860417L;
    private transient TextChannel systemChannel;
    private transient TextChannel commandLogChannel;


    public void load() {
        systemChannel = DiscordModule.dcf.jda().getTextChannelById(systemChannelId);
        commandLogChannel = DiscordModule.dcf.jda().getTextChannelById(commandLogChannelId);
        assert systemChannel != null;
        assert commandLogChannel != null;
    }

    public String getToken() {
        return token;
    }

    public TextChannel getReportsChannel() {
        return systemChannel;
    }

    public TextChannel getStatusChannel() {
        return systemChannel;
    }

    public TextChannel getCommandLogChannel() {
        return commandLogChannel;
    }

    public boolean isDevServer(Guild guild) {
        if (guild == null) return false;
        return devServerId == guild.getIdLong();
    }
}
