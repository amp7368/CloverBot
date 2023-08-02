package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import java.time.Instant;

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
}
