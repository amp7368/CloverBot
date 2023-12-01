package apple.discord.clover.wynncraft.network;

import java.util.List;
import java.util.Map;

public class ServerListResponse {

    public Map<String, String> players;
    public ServerListResponseTimestamp request;

    public ServerListResponse(Map<String, String> players, ServerListResponseTimestamp responseMeta) {
        this.players = players;
        this.request = responseMeta;
    }

    public List<String> getPlayers() {
        return List.copyOf(players.keySet());
    }
}
