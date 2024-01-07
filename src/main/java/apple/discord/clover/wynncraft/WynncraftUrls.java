package apple.discord.clover.wynncraft;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class WynncraftUrls {

    public static final String PLAYER_STATS = "https://api.wynncraft.com/v3/player/%s?fullResult=True";
    public static final String GUILD_STATS = "https://api.wynncraft.com/v3/guild/%s?identifier=uuid";
    public static final String GUILD_LIST = "https://api.wynncraft.com/v3/guild/list/guild";
    public static final String SERVER_LIST = "https://api.wynncraft.com/v3/player?identifier=uuid";

    public static String playerStats(String player) {
        return PLAYER_STATS.formatted(normalize(player));
    }

    public static String playerStats(UUID player) {
        return PLAYER_STATS.formatted(player);
    }

    public static String guild(String guild) {
        return GUILD_STATS.formatted(normalize(guild));
    }

    @NotNull
    private static String normalize(String guild) {
        return guild.replace(" ", "%20");
    }

    public static class Status {

        public static final int BAD_REQUEST = 400;
        public static final int NOT_FOUND = 404;
        public static final int MULTIPLE = 300;
        public static final int TOO_MANY_REQUESTS = 429;
    }
}
