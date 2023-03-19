package apple.discord.clover.service.network;

import apple.discord.clover.database.activity.partial.LoginStorage;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftApi.Status;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.discord.clover.wynncraft.network.ServerListResponse;
import apple.discord.clover.wynncraft.network.ServerListResponseTimestamp;
import apple.discord.clover.wynncraft.response.RepeatThrottle;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.priority.AsyncTaskPriority;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import discord.util.dcf.util.TimeMillis;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.Logger;

public class ServiceServerList {

    private static final Builder SERVER_LIST_REQUEST = new Builder().url(WynncraftApi.SERVER_LIST)
        .cacheControl(CacheControl.FORCE_NETWORK);
    private static final AsyncTaskQueueStart<AsyncTaskPriority> SERVICE = WynncraftRatelimit.getNetwork()
        .taskCreator(new AsyncTaskPriority(TaskPriorityCommon.LOW));
    private static final long REPEAT_INTERVAL = TimeMillis.minToMillis(5);

    private final OkHttpClient http = new OkHttpClient();
    private final RepeatThrottle throttle = new RepeatThrottle(5000);

    public ServiceServerList() {
        new Thread(this::run).start();
    }

    private void run() {
        while (true) {
            try {
                this.daemon();
                if (!throttle.doSleepBuffer()) {
                    //noinspection BusyWait
                    Thread.sleep(REPEAT_INTERVAL);
                }
            } catch (Exception e) {
                logger().error(e);
            }
        }
    }

    private void daemon() {
        SERVICE.accept(this::call, this::queuePlayers, (e) -> this.logger().error(e));
    }

    private void queuePlayers(ServerListResponse response) {
        if (response == null) return;
        Instant requestedAt = Instant.ofEpochSecond(response.request.timestamp);
        new Thread(() -> LoginStorage.queuePlayers(response.players, requestedAt)).start();
    }

    private Logger logger() {
        return ServiceModule.get().logger();
    }

    private ServerListResponse call() throws IOException {
        Call call = http.newCall(SERVER_LIST_REQUEST.get().build());
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                throttle.incrementError();
                if (response.code() == Status.TOO_MANY_REQUESTS) {
                    logger().warn("Rate limit reached: %d error(s) in a row".formatted(throttle.getErrorCount()));
                } else {
                    ResponseBody body = response.body();
                    String message = body == null ? "No body" : body.string();
                    logger().error("Response code: %d, Body: %s".formatted(response.code(), message));
                }
                return null;
            }
            Type responseType = TypeToken.getParameterized(Map.class, String.class, String[].class).getType();
            ResponseBody body = response.body();
            if (body == null) {
                logger().error("Ok response, but had no body");
                return null;
            }
            JsonObject json = WynncraftRatelimit.gson().fromJson(body.charStream(), JsonObject.class);
            List<String> players = new ArrayList<>();
            ServerListResponseTimestamp responseMeta = null;
            for (String worldName : json.keySet()) {
                JsonElement jsonValue = json.get(worldName);
                if (worldName.equals("request")) {
                    responseMeta = WynncraftRatelimit.gson().fromJson(jsonValue,
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
    }

}
