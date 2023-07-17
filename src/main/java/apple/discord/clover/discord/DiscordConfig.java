package apple.discord.clover.discord;

public class DiscordConfig {

    private static DiscordConfig instance;
    public String token = "YourTokenHere";
    protected long reportsChannel = 0;

    public DiscordConfig() {
        instance = this;
    }

    public static DiscordConfig get() {
        return instance;
    }

    public String getToken() {
        return token;
    }
}
