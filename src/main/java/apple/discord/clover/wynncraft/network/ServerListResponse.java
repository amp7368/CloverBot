package apple.discord.clover.wynncraft.network;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerListResponse {

    public Map<String, List<UUID>> players;

    public ServerListResponse(Map<String, List<UUID>> players) {
        this.players = players;
    }
}
