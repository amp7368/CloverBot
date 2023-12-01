package apple.discord.clover.wynncraft.overview.guild;

import java.util.Arrays;

public class WynncraftGuildListResponse {

    protected WynncraftGuildListEntry[] guilds;

    public WynncraftGuildListResponse(WynncraftGuildListEntry[] guilds) {
        this.guilds = guilds;
    }

    public WynncraftGuildListEntry[] getGuilds() {
        return Arrays.copyOf(guilds, guilds.length);
    }

    public boolean hasGuilds() {
        return guilds != null && guilds.length > 0;
    }
}
