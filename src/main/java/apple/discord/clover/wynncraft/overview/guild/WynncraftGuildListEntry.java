package apple.discord.clover.wynncraft.overview.guild;

import apple.discord.clover.database.guild.DGuild;
import com.google.common.base.Objects;
import java.util.UUID;

public class WynncraftGuildListEntry {

    private String name;
    private String prefix;
    private UUID uuid;

    public WynncraftGuildListEntry() {
    }

    public WynncraftGuildListEntry(DGuild guild) {
        this.name = guild.getName();
        this.prefix = guild.getTag();
    }


    @Override
    public boolean equals(Object o) {
        // todo
        if (!(o instanceof WynncraftGuildListEntry other)) return false;
        return Objects.equal(name, other.name) && Objects.equal(prefix, other.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, prefix);
    }

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return this.prefix;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
}
