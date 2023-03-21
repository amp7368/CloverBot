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
import discord.util.dcf.util.TimeMillis;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    private final static long ERROR_MARGIN = 100;
    private final OkHttpClient http = new OkHttpClient();
    private final RepeatThrottle throttle = new RepeatThrottle(5000);

    public ServiceServerList() {
        ServiceServerListConfig.sleepIfLastQueryRecent(REPEAT_INTERVAL);
        new Thread(this::run).start();
    }

    private void run() {
        while (true) {
            try {
                long start = System.currentTimeMillis();
                this.daemon();
                ServiceServerListConfig.updateLastQuery();
                long timeTaken = System.currentTimeMillis() - start;
                long sleep = throttle.getSleepBuffer(REPEAT_INTERVAL - timeTaken);
                logger().info("Sleeping for %d millis".formatted(sleep));
                //noinspection BusyWait
                Thread.sleep(sleep + ERROR_MARGIN);
            } catch (Exception e) {
                logger().error("==ServiceServerList==", e);
            }
        }
    }

    private void daemon() {
        SERVICE.accept(this::call, this::queuePlayers, (e) -> this.logger().error(e));
    }

    private void queuePlayers(ServerListResponse response) {
        if (response == null) return;
        throttle.incrementSuccess();
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
                    String message = body.string();
                    logger().error("Response code: %d, Body: %s".formatted(response.code(), message));
                }
                return null;
            }
            JsonObject json = WynncraftRatelimit.gson().fromJson(response.body().charStream(), JsonObject.class);
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
