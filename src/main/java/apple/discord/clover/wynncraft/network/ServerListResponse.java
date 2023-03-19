package apple.discord.clover.wynncraft.network;

import java.util.List;

public class ServerListResponse {

    public List<String> players;
    public ServerListResponseTimestamp request;

    public ServerListResponse(List<String> players, ServerListResponseTimestamp responseMeta) {
        this.players = players;
        this.request = responseMeta;
    }
}
