package apple.discord.clover.discord;

public class DiscordConfig {

    private static DiscordConfig instance;
    public String token = "YourTokenHere";

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
