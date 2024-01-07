package apple.discord.clover.wynncraft.network;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ServerListResponse {

    public Map<UUID, String> players;
    public ServerListResponseTimestamp request;

    public ServerListResponse() {
    }

    public List<UUID> getPlayers() {
        return List.copyOf(players.keySet());
    }
}
