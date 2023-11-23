package apple.discord.clover.service.network;

import apple.discord.clover.database.activity.partial.LoginStorage;
import apple.discord.clover.service.base.DaemonService;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.network.ServerListResponse;
import apple.discord.clover.wynncraft.network.ServerListResponseTimestamp;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import discord.util.dcf.util.TimeMillis;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import okhttp3.CacheControl;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class ServiceServerList extends DaemonService<ServerListResponse> {

    public static final long SERVER_LIST_OFFLINE_INTERVAL = TimeMillis.minToMillis(5);
    private static final Builder SERVER_LIST_REQUEST = new Builder().get()
        .url(WynncraftApi.SERVER_LIST)
        .cacheControl(CacheControl.FORCE_NETWORK);

    public ServiceServerList() {
        queueStart();
    }


    @Override
    protected void start() {
        ServiceServerListConfig.get().sleepIfLastQueryRecent(normalInterval());
        super.start();
    }

    @Override
    protected void updateLastQuery() {
        ServiceServerListConfig.get().updateLastQuery();
    }

    @Override
    protected void acceptResponse(ServerListResponse response) {
        if (response == null) return;
        throttle.incrementSuccess();
        Instant requestedAt = Instant.ofEpochSecond(response.request.timestamp);
        new Thread(() -> LoginStorage.queuePlayers(response.players, requestedAt)).start();
    }

    @NotNull
    protected ServerListResponse deserialize(Response response) {
        JsonObject json = WynncraftModule.gson().fromJson(response.body().charStream(), JsonObject.class);
        List<String> players = new ArrayList<>();
        ServerListResponseTimestamp responseMeta = null;
        for (String worldName : json.keySet()) {
            JsonElement jsonValue = json.get(worldName);
            if (worldName.equals("request")) {
                responseMeta = WynncraftModule.gson().fromJson(jsonValue,
                    ServerListResponseTimestamp.class);
                continue;
            }
            JsonArray array = jsonValue.getAsJsonArray();
            for (JsonElement player : array) {
                players.add(player.getAsString());
            }
        }
        return new ServerListResponse(players, responseMeta);
    }

    @Override
    protected Builder request() {
        return SERVER_LIST_REQUEST;
    }

    @Override
    protected long normalInterval() {
        return SERVER_LIST_OFFLINE_INTERVAL;
    }
}
