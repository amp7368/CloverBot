package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.query.player.PlayerStatsQuery;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import java.time.Instant;

public class InactiveDPlayer extends InactivePlayer {

    private final DPlayer player;

    public InactiveDPlayer(WynnGuildMember guildMember, DPlayer player) {
        super(guildMember);
        this.player = player;
    }

    @Override
    public Instant getLastJoin() {
        return PlayerStatsQuery.findLastSession(player).retrievedTime.toInstant();
    }

    @Override
    public String getName() {
        return player.username;
    }
}
