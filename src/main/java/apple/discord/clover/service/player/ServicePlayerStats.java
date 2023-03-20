package apple.discord.clover.service.player;

import apple.discord.clover.database.activity.blacklist.BlacklistStorage;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.activity.partial.LoginStorage;
import apple.discord.clover.database.player.PlayerStorage;
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
import apple.utilities.util.NumberUtils;
import discord.util.dcf.util.TimeMillis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.exceptions.HttpException;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.Logger;

public class ServicePlayerStats {

    private static final AsyncTaskQueueStart<AsyncTaskPriority> SERVICE = WynncraftRatelimit.getPlayer()
        .taskCreator(new AsyncTaskPriority(TaskPriorityCommon.LOW));
    private static final int REQUESTS = 750;
    private static final long REPEAT_INTERVAL = TimeMillis.minToMillis(30) / REQUESTS;
    private static ServicePlayerStats instance;
    private final OkHttpClient http = new OkHttpClient();
    private final RepeatThrottle throttle = new RepeatThrottle(5000);
    private final List<DLoginQueue> nextPlayers = new ArrayList<DLoginQueue>();

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
        SERVICE.accept(this::call, this::updatePlayer, (e) -> this.logger().error("", e));
    }

    private void updatePlayer(PlaySessionRaw response) {
        if (response == null) return;
        WynnPlayer player = response.getPlayer();
        if (player == null) {
            LoginStorage.failure(response.login());
            return;
        }
        LoginStorage.success(response.login());
        PlayerStorage.save(response.login(), player);
    }

    private Logger logger() {
        return ServiceModule.get().logger();
    }

    private PlaySessionRaw call() throws IOException {
        logger().info("Checking for available players to queue");
        if (nextPlayers.isEmpty()) {
            List<DLoginQueue> updates = LoginStorage.findUpdates();
            if (updates.isEmpty()) return null;
            this.nextPlayers.addAll(updates);
        }
        DLoginQueue nextPlayer = this.nextPlayers.remove(0);
        logger().info("Downloading " + nextPlayer.player);
        Call call = http.newCall(new Builder().url(WynncraftApi.playerStats(nextPlayer.player))
            .cacheControl(CacheControl.FORCE_NETWORK).get().build());
        try (Response httpResponse = call.execute()) {
            if (!httpResponse.isSuccessful()) {
                throttle.incrementError();
                int code = httpResponse.code();
                if (code == Status.TOO_MANY_REQUESTS) {
                    throw new HttpException("Rate limit reached: %d error(s) in a row".formatted(throttle.getErrorCount()));
                } else if (NumberUtils.between(400, code, 500)) {
                    BlacklistStorage.failure(nextPlayer);
                }
                ResponseBody body = httpResponse.body();
                throw new HttpException("Response code: %d, Body: %s".formatted(code, body.string()));
            }
            ResponseBody body = httpResponse.body();
            WynnPlayerResponse response = WynncraftRatelimit.gson().fromJson(body.charStream(), WynnPlayerResponse.class);
            return new PlaySessionRaw(nextPlayer, response);
        }
    }

}
