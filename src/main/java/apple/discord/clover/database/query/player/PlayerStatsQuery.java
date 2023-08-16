package apple.discord.clover.database.query.player;

import apple.discord.clover.api.player.request.PlayerRequest;
import apple.discord.clover.api.player.stats.response.PlayerStatsResponse;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.query.QDPlaySession;
import apple.discord.clover.database.player.DPlayer;

public class PlayerStatsQuery {

    public static PlayerStatsResponse queryPlayerStats(PlayerRequest request) {
        return new PlayerStatsResponse();
    }

    public static DPlaySession findLastSession(DPlayer player) {
        return new QDPlaySession()
            .where()
            .player.eq(player)
            .orderBy().retrievedTime.desc()
            .setMaxRows(1)
            .findOne();
    }
}
