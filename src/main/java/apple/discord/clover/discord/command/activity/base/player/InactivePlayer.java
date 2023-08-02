package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import java.time.Duration;
import java.time.Instant;

public abstract class InactivePlayer {


    protected final WynnGuildMember guildMember;

    public InactivePlayer(WynnGuildMember guildMember) {
        this.guildMember = guildMember;
    }

    public abstract Instant getLastJoin();

    public abstract String getName();

    public Duration getInactiveDuration() {
        Instant lastJoin = getLastJoin();
        if (lastJoin == null) return null;
        return Duration.between(lastJoin, Instant.now());
    }

    public String getGuildRank() {
        return guildMember.rank;
    }
}
