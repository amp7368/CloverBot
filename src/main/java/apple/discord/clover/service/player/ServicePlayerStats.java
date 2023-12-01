package apple.discord.clover.service.player;

import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.activity.partial.LoginStorage;
import apple.discord.clover.database.player.PlayerStorage;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynnHeaders;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftApi.Status;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.overview.guild.response.RepeatThrottle;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.utilities.util.NumberUtils;
import discord.util.dcf.util.TimeMillis;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.dv8tion.jda.api.exceptions.HttpException;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ServicePlayerStats {

    private static final int REQUESTS = 250;
    private static final long REPEAT_INTERVAL = TimeMillis.minToMillis(5) / REQUESTS;
    private static final Duration CALL_TIMEOUT = Duration.ofSeconds(30);
    private static ServicePlayerStats instance;
    private final OkHttpClient http = new OkHttpClient.Builder().callTimeout(CALL_TIMEOUT).build();
    private final RepeatThrottle throttle = new RepeatThrottle(5000);
    private final List<DLoginQueue> nextPlayers = new ArrayList<>();

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
                long start = System.currentTimeMillis();
                this.daemon();
                long timeTaken = System.currentTimeMillis() - start;
                long sleep = throttle.getSleepBuffer(REPEAT_INTERVAL - timeTaken);
                logger().info("Sleeping for %d millis".formatted(sleep));
                //noinspection BusyWait
                Thread.sleep(sleep);
            } catch (Exception e) {
                throttle.incrementError();
                logger().error("==ServicePlayerStats==", e);
            }
        }
    }

    private void daemon() {
        PlaySessionRaw response;
        try {
            response = this.call();
        } catch (Exception e) {
            this.logger().error("", e);
            return;
        }
        try {
            this.updatePlayer(response);
        } catch (Exception e) {
            throttle.incrementError();
            this.logger().error("", e);
        }
    }

    private synchronized void updatePlayer(PlaySessionRaw response) {
        try {
            if (response == null) return;
            WynnPlayer player = response.getPlayer();
            if (player == null) {
                LoginStorage.failure(response.login());
                return;
            }
            throttle.incrementSuccess();
            boolean updated = PlayerStorage.save(response.login(), player);
            if (updated) LoginStorage.success(response.login());
            else LoginStorage.failure(response.login());
        } catch (Exception e) {
            this.logger().error("", e);
        }
    }

    private Logger logger() {
        return ServiceModule.get().logger();
    }

    private synchronized PlaySessionRaw call() throws IOException {
        if (nextPlayers.isEmpty()) {
            logger().info("Caching queue with more players from LoginStorage");
            Collection<DLoginQueue> updates = LoginStorage.findUpdates();
            if (updates.isEmpty()) return null;
            this.nextPlayers.addAll(updates);
        }
        logger().info("Players left in cached queue: {}", nextPlayers.size());
        DLoginQueue nextPlayer = this.nextPlayers.remove(0);
        logger().info("Downloading " + nextPlayer.player);
        Call call = http.newCall(request(nextPlayer.player).build());
        try (Response httpResponse = call.execute()) {
            if (!httpResponse.isSuccessful()) {
                int code = httpResponse.code();
                if (code == 500) {
                    // https://api.wynncraft.com/v3/player/FakeUsername
                    // When they fix this error response, remove this code
                    LoginStorage.failure(nextPlayer);
                    return null;
                }
                throttle.incrementError();
                if (code == Status.TOO_MANY_REQUESTS) {
                    throw new HttpException("Rate limit reached: %d error(s) in a row".formatted(throttle.getErrorCount()));
                } else if (NumberUtils.between(400, code, 500)) {
                    LoginStorage.failure(nextPlayer);
                    return null;
                }
                // unexpected errors

                ResponseBody body = httpResponse.body();
                System.err.println(call.request());
                throw new HttpException("Response code: %d, Body: %s".formatted(code, body.string()));
            }
            ResponseBody body = httpResponse.body();
            WynnPlayer response = WynncraftModule.gson().fromJson(body.charStream(), WynnPlayer.class);
            response.setRetrieved(WynnHeaders.date(httpResponse));
            return new PlaySessionRaw(nextPlayer, response);
        }
    }

    @NotNull
    private Builder request(String nextPlayer) {
        return new Builder().url(WynncraftApi.playerStats(nextPlayer))
            .cacheControl(CacheControl.FORCE_NETWORK).get();
    }

}
