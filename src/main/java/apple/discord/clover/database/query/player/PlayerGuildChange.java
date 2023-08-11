package apple.discord.clover.database.query.player;

import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.player.guild.GuildStorage;
import apple.utilities.util.ObjectUtilsFormatting;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.jetbrains.annotations.UnknownNullability;

public class PlayerGuildChange {

    protected UUID currentGuild;
    protected UUID lastGuild;
    protected Timestamp retrievedTime;
    private transient DGuild lastGuildObj;
    private transient DGuild currentGuildObj;

    public PlayerGuildChange(UUID lastGuild, UUID currentGuild, Timestamp retrievedTime) {
        this.currentGuild = currentGuild;
        this.lastGuild = lastGuild;
        this.retrievedTime = retrievedTime;
    }

    public DGuild getCurrentGuild() {
        if (currentGuild == null) return null;
        if (currentGuildObj != null) return currentGuildObj;
        return this.currentGuildObj = GuildStorage.findById(this.currentGuild);
    }

    @UnknownNullability
    public DGuild getLastGuild() {
        if (lastGuild == null) return null;
        if (lastGuildObj != null) return lastGuildObj;
        return this.lastGuildObj = GuildStorage.findById(this.lastGuild);
    }

    public Instant getRetrievedTime() {
        return retrievedTime.toInstant();
    }

    @Override
    public String toString() {
        String last = ObjectUtilsFormatting.failToNull(getLastGuild(), DGuild::getName);
        String current = ObjectUtilsFormatting.failToNull(getCurrentGuild(), DGuild::getName);
        String time = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault()).format(getRetrievedTime());
        return "%s -> %s [%s]".formatted(last, current, time);
    }

    public boolean hasCurrentGuild() {
        return this.getCurrentGuild() != null;
    }

    public boolean hasLastGuild() {
        return this.getLastGuild() != null;
    }
}
