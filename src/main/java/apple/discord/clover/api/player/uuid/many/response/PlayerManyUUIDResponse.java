package apple.discord.clover.api.player.uuid.many.response;

import apple.discord.clover.api.player.uuid.one.response.PlayerOneUUIDResponse;
import apple.discord.clover.database.player.DPlayer;
import java.util.List;

public class PlayerManyUUIDResponse {

    private List<PlayerOneUUIDResponse> players;

    public PlayerManyUUIDResponse(List<DPlayer> players) {
        this.players = players.stream().map(PlayerOneUUIDResponse::new).toList();
    }
}
