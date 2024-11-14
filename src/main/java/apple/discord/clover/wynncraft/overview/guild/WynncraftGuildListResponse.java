package apple.discord.clover.wynncraft.overview.guild;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

public class WynncraftGuildListResponse {

    protected Map<UUID, WynncraftGuildListEntry> guilds;

    public WynncraftGuildListResponse(Map<UUID, WynncraftGuildListEntry> guilds) {
        this.guilds = guilds;
        for (Entry<UUID, WynncraftGuildListEntry> entry : guilds.entrySet()) {
            entry.getValue().setUUID(entry.getKey());
        }
    }

    public WynncraftGuildListEntry[] getGuilds() {
        return guilds.values().toArray(WynncraftGuildListEntry[]::new);
    }

    public boolean hasGuilds() {
        return guilds != null && !guilds.isEmpty();
    }
}
