package apple.discord.clover.wynncraft.stats.guild;

import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.UUID;

public class WynnGuildMember {

    public UUID uuid;
    public String rank;
    public String username;
    @SerializedName(value = "contributed")
    public long xpContributed;
    public int contributionRank;
    public Date joined;

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WynnGuildMember other) {
            if (this.uuid == null || other.uuid == null)
                throw new IllegalStateException("UUID must be set before allowing #equals");
            return uuid.equals(other.uuid);
        }
        return false;

    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
