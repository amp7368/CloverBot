package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.api.player.terms.request.PlayerTermsRequest;
import apple.discord.clover.api.player.terms.response.PlayerTermsResponse;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.query.player.PlayerStatsQuery;
import apple.discord.clover.database.query.player.PlayerTermsQuery;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import java.time.Duration;
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
    public Duration queryPlaytime(InactivePlayerQueryPlaytime query) {
        Instant start = Instant.now().minus(query.resolution().duration(query.termsAfter()));
        PlayerTermsRequest request = new PlayerTermsRequest(query.resolution(), start, query.termsAfter(), this.player);
        PlayerTermsResponse response = PlayerTermsQuery.queryPlayerTerms(request);
        long playtime = response.playtime();
        return Duration.ofMinutes(playtime);
    }

    @Override
    public String getName() {
        return player.username;
    }
}
