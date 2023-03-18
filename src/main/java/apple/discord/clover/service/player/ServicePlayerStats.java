package apple.discord.clover.service.player;

import apple.discord.clover.database.activity.partial.LoginStorage;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftApi.Status;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.discord.clover.wynncraft.response.RepeatThrottle;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.discord.clover.wynncraft.stats.player.WynnPlayerResponse;
import apple.utilities.threading.service.base.create.AsyncTaskQueueStart;
import apple.utilities.threading.service.priority.AsyncTaskPriority;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import com.google.gson.Gson;
import discord.util.dcf.util.TimeMillis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServicePlayerStats {

    private static final AsyncTaskQueueStart<AsyncTaskPriority> SERVICE = WynncraftRatelimit.getPlayer()
        .taskCreator(new AsyncTaskPriority(TaskPriorityCommon.LOW));
    private static final int REQUESTS = 750;
    private static final long REPEAT_INTERVAL = TimeMillis.minToMillis(30) / REQUESTS;
    private static ServicePlayerStats instance;
    private final OkHttpClient http = new OkHttpClient();
    private final RepeatThrottle throttle = new RepeatThrottle(1000);
    private final List<String> nextPlayers = new ArrayList<>();

    public ServicePlayerStats() {
        instance = this;
        new Thread(this::run).start();
    }

    public static ServicePlayerStats get() {
        return instance;
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
                logger().error("==ServicePlayerStats==", e);
            }
        }
    }

    private void daemon() {
        if (nextPlayers.isEmpty()) {
            @NotNull List<String> updates = LoginStorage.findUpdates();
            if (updates.isEmpty()) return;
            this.nextPlayers.addAll(updates);
        }
        SERVICE.accept(this::call, this::updatePlayer, (e) -> this.logger().error("", e));
    }

    private void updatePlayer(WynnPlayerResponse response) {
        if (response == null) return;
        WynnPlayer player = response.getPlayer();
        if (player == null) return;
        LoginStorage.remove(player.username);
        System.err.println(new Gson().toJson(response));
    }

    private Logger logger() {
        return ServiceModule.get().logger();
    }

    @Nullable
    private WynnPlayerResponse call() throws IOException {
        if (this.nextPlayers.isEmpty()) return null;
        String nextPlayer = this.nextPlayers.remove(0);
        Call call = http.newCall(new Builder().url(WynncraftApi.playerStats(nextPlayer))
            .cacheControl(CacheControl.FORCE_NETWORK).get().build());
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
            ResponseBody body = response.body();
            if (body == null) {
                logger().error("Ok response, but had no body");
                return null;
            }
            return WynncraftRatelimit.gson().fromJson(body.charStream(), WynnPlayerResponse.class);
        }
    }

}
