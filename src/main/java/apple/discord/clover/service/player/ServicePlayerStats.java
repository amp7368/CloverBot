package apple.discord.clover.service.player;

import apple.discord.clover.database.player.PlayerStorage;
import apple.discord.clover.database.queue.partial.DLoginQueue;
import apple.discord.clover.database.queue.partial.LoginStorage;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.WynncraftUrls;
import apple.discord.clover.wynncraft.WynncraftUrls.Status;
import apple.discord.clover.wynncraft.overview.guild.response.RepeatThrottle;
import apple.discord.clover.wynncraft.response.WynnHeaders;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import discord.util.dcf.util.TimeMillis;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.dv8tion.jda.api.exceptions.HttpException;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ServicePlayerStats {

    private static final int REQUESTS = 250;
    private static final long REPEAT_INTERVAL = TimeMillis.minToMillis(5) / REQUESTS;
    private static final Duration CALL_TIMEOUT = Duration.ofSeconds(60);
    private static ServicePlayerStats instance;
    private final OkHttpClient http = new OkHttpClient.Builder()
        .callTimeout(CALL_TIMEOUT)
        .readTimeout(CALL_TIMEOUT)
        .writeTimeout(CALL_TIMEOUT)
        .build();
    private final RepeatThrottle throttle = new RepeatThrottle(5000);
    private final List<DLoginQueue> nextPlayers = new ArrayList<>();
    private final List<LoginQueueUUID> nextPlayerUUIDs = new ArrayList<>();

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
        if (!nextPlayerUUIDs.isEmpty()) {
            return handleNextPlayerUUID(nextPlayerUUIDs.removeFirst());
        }
        if (nextPlayers.isEmpty()) {
            logger().info("Caching queue with more players from LoginStorage");
            Collection<DLoginQueue> updates = LoginStorage.findUpdates();
            if (updates.isEmpty()) return null;
            this.nextPlayers.addAll(updates);
        }
        logger().info("Players left in cached queue: {}", nextPlayers.size());
        DLoginQueue nextPlayer = this.nextPlayers.removeFirst();
        logger().info("Downloading {}", nextPlayer.player);
        Call call = http.newCall(request(nextPlayer.player).build());
        try (Response httpResponse = call.execute()) {
            if (!httpResponse.isSuccessful()) {
                handleErrorResponse(httpResponse, nextPlayer, call);
                return null;
            }
            return handleSuccessResponse(httpResponse, nextPlayer);
        } catch (UnknownHostException e) {
            logger().warn("No connection to Wynncraft when trying {}", nextPlayer.player);
            // will be readded to DLoginQueueCache on next LoginStorage.findUpdates()
            return null;
        }
    }

    private PlaySessionRaw handleNextPlayerUUID(LoginQueueUUID nextUUID) throws IOException {
        UUID nextPlayerUUID = nextUUID.popUUID();

        logger().info("Downloading UUID {}", nextPlayerUUID);
        Call call = http.newCall(request(nextPlayerUUID.toString()).build());
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                handleErrorResponse(response, nextUUID.login(), call);
                return null;
            }

            String body = response.body().string();
            WynnPlayer player = jsonWynnPlayer(body);
            if (player == null || player.isNullData()) {
                if (!nextUUID.isEmpty()) nextPlayerUUIDs.add(nextUUID);
                return null;
            }
            // this is not the real player
            Instant dayAgo = Instant.now().minus(Duration.ofDays(1));
            if (player.getLastJoin().isBefore(dayAgo)) {
                if (!nextUUID.isEmpty()) nextPlayerUUIDs.add(nextUUID);
                return null;
            }

            player.setVersion(WynnHeaders.version(response));
            player.setRetrieved(WynnHeaders.date(response));
            return new PlaySessionRaw(nextUUID.login(), player);
        } catch (UnknownHostException e) {
            logger().warn("No connection to Wynncraft when trying {}", nextPlayerUUID);
            // will be readded to DLoginQueueCache on next LoginStorage.findUpdates()
            return null;
        }
    }

    private PlaySessionRaw handleSuccessResponse(Response response, DLoginQueue nextPlayer) throws IOException {
        String body = response.body().string();
        WynnPlayer player = jsonWynnPlayer(body);
        if (player == null || player.isNullData()) {
            Map<String, JsonObject> manyPlayerMap = jsonManyPlayerMap(body);
            if (manyPlayerMap == null)
                return new PlaySessionRaw(nextPlayer, null);
            nextPlayerUUIDs.add(new LoginQueueUUID(nextPlayer, manyPlayerMap.keySet().stream().map(UUID::fromString).toList()));
            return null;
        }

        player.setVersion(WynnHeaders.version(response));
        player.setRetrieved(WynnHeaders.date(response));
        return new PlaySessionRaw(nextPlayer, player);
    }


    private void handleErrorResponse(Response httpResponse, DLoginQueue nextPlayer, Call call) throws IOException {
        int code = httpResponse.code();
        if (code == 500) {
            // https://api.wynncraft.com/v3/player/FakeUsername
            // When they fix this error response, remove this code
            LoginStorage.failure(nextPlayer);
            return;
        }
        String body = httpResponse.body().string();

        if (code == Status.NOT_FOUND) {
            LoginStorage.failure(nextPlayer);
            ServiceModule.get().logger().warn("404 Not Found: " + nextPlayer.player);
            return;
        }
        throttle.incrementError();
        if (code == Status.TOO_MANY_REQUESTS) {
            throw new HttpException("Rate limit reached: %d error(s) in a row".formatted(throttle.getErrorCount()));
        }
        // unexpected errors
        logger().error(call.request());
        throw new HttpException("Response code: %d, Body: %s".formatted(code, body));
    }

    private Map<String, JsonObject> jsonManyPlayerMap(String reader) {
        try {
            TypeToken<?> clazz = TypeToken.getParameterized(Map.class, String.class, JsonObject.class);
            return WynncraftModule.gson().fromJson(reader, clazz.getType());
        } catch (JsonParseException ignored) {
            return null;
        }
    }

    private WynnPlayer jsonWynnPlayer(String reader) {
        try {
            return WynncraftModule.gson().fromJson(reader, WynnPlayer.class);
        } catch (JsonParseException ignored) {
            return null;
        }
    }

    @NotNull
    private Builder request(String nextPlayer) {
        return new Builder().url(WynncraftUrls.playerStats(nextPlayer))
            .cacheControl(CacheControl.FORCE_NETWORK).get();
    }

}
