package apple.discord.clover.wynncraft;

public class WynncraftApi {

    public static final String GUILD_LIST = "https://api.wynncraft.com/public_api.php?action=guildList";
    public static final String PLAYER_STATS = "https://api.wynncraft.com/v2/player/%s/stats";
    public static final String GUILD_STATS = "https://api.wynncraft.com/public_api.php?action=guildStats&command=%s";
    public static final String SERVER_LIST = "https://api.wynncraft.com/public_api.php?action=onlinePlayers";

    public static String playerStats(String player) {
        return PLAYER_STATS.formatted(player);
    }

    public static class Status {

        public static final int BAD_REQUEST = 400;
        public static final int NOT_FOUND = 404;
        public static final int TOO_MANY_REQUESTS = 429;
    }
}
