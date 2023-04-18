package apple.discord.clover.database.query.player;

import apple.discord.clover.api.player.request.PlayerRequest;
import apple.discord.clover.api.player.stats.response.PlayerStatsResponse;

public class PlayerStatsQuery {

    public static PlayerStatsResponse queryPlayerStats(PlayerRequest request) {
        return new PlayerStatsResponse();
    }
}
