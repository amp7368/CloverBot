package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import java.time.Duration;
import java.time.Instant;
import org.jetbrains.annotations.Nullable;

public class InactiveWynnPlayer extends InactivePlayer {

    private final WynnPlayer player;

    public InactiveWynnPlayer(WynnGuildMember guildMember, @Nullable WynnPlayer player) {
        super(guildMember);
        this.player = player;
    }


    @Override
    @Nullable
    public Instant getLastJoin() {
        if (player == null) return null;
        return player.meta.lastJoin.toInstant();
    }

    @Override
    public String getName() {
        if (player == null) return guildMember.name;
        return player.username;
    }

    @Override
    @Nullable
    public Duration queryPlaytime(InactivePlayerQueryPlaytime playtime) {
        return Duration.ZERO;
    }
}
