package apple.discord.clover.wynncraft.stats.guild;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.UUID;

public class WynnGuildMember {

    public UUID uuid;
    public String name;
    public String rank;
    @SerializedName(value = "contributed")
    public long xpContributed;
    public Date joined;

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WynnGuildMember other) {
            return uuid.equals(other.uuid);
        }
        return false;

    }
}
