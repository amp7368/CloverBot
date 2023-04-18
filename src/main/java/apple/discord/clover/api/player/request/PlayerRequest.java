package apple.discord.clover.api.player.request;

import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;

public class PlayerRequest implements HasPlayerRequest {

    private transient final PlayerNameOrUUID nameOrUUID = new PlayerNameOrUUID();
    private String player;

    @Override
    public PlayerNameOrUUID getPlayer() {
        return this.nameOrUUID;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public String getPlayerString() {
        return player;
    }
}
