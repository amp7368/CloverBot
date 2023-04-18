package apple.discord.clover.api.player.uuid.one.response;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.database.player.DPlayer;
import java.util.UUID;

public class PlayerOneUUIDResponse {

    private final String player;
    private final UUID uuid;

    public PlayerOneUUIDResponse(PlayerNameOrUUID player) {
        this.uuid = player.uuid();
        this.player = player.username();
    }

    public PlayerOneUUIDResponse(DPlayer player) {
        this.uuid = player.uuid;
        this.player = player.username;
    }
}
