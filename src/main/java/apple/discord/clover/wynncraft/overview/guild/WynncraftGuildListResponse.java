package apple.discord.clover.wynncraft.overview.guild;

import java.util.Arrays;

public class WynncraftGuildListResponse {

    protected String[] guilds;

    public String[] getGuilds() {
        return Arrays.stream(guilds).toArray(String[]::new);
    }

    public boolean hasGuilds() {
        return guilds != null && guilds.length > 0;
    }
}
