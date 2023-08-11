package apple.discord.clover.api.base.request;

import io.javalin.http.NotFoundResponse;
import org.jetbrains.annotations.Nullable;

public class FindPlayerFromString implements HasPlayerRequest {

    private final String playerString;
    private final PlayerNameOrUUID player = new PlayerNameOrUUID();

    public FindPlayerFromString(String playerString) {
        this.playerString = playerString;
        this.fetchPlayer();
    }

    @Nullable
    public static PlayerNameOrUUID find(String playerString) {
        PlayerNameOrUUID player = new FindPlayerFromString(playerString).getPlayer();
        try {
            player.checkNotNull();
            return player;
        } catch (NotFoundResponse e) {
            return null;
        }
    }

    @Override
    public PlayerNameOrUUID getPlayer() {
        return this.player;
    }

    @Override
    public String getPlayerString() {
        return playerString;
    }
}
