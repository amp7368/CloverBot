package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import java.time.Duration;
import java.time.Instant;
import org.jetbrains.annotations.Nullable;

public class InactiveNotFoundPlayer extends InactivePlayer {

    public InactiveNotFoundPlayer(WynnGuildMember guildMember) {
        super(guildMember);
    }

    @Override
    public Instant getLastJoin() {
        return Instant.now();
    }

    @Override
    public String getName() {
        if (guildMember == null) return "???";
        else return guildMember.name;
    }

    @Override
    @Nullable
    public Duration queryPlaytime(InactivePlayerQueryPlaytime playtime) {
        return null;
    }
}
