package apple.discord.clover.wynncraft;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class WynncraftUrls {

    private static final String baseURL = "https://api.wynncraft.com/v3";
    public static final String PLAYER_STATS = baseURL + "/player/%s?fullResult=True";
    public static final String GUILD_STATS = baseURL + "/guild/%s?identifier=uuid";
    public static final String GUILD_LIST = baseURL + "/guild/list/guild?identifier=uuid";
    public static final String SERVER_LIST = baseURL + "/player?identifier=uuid";

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
