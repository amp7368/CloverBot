package apple.discord.clover.service.base;

import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynncraftUrls.Status;
import apple.discord.clover.wynncraft.overview.guild.response.RepeatThrottle;
import apple.discord.clover.wynncraft.response.WynnResponse;
import com.google.gson.Gson;
import java.time.Duration;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.Logger;

public abstract class DaemonService<Res> implements Runnable {

    protected static final Duration CALL_TIMEOUT = Duration.ofSeconds(60);
    protected final OkHttpClient http = new OkHttpClient.Builder()
        .callTimeout(CALL_TIMEOUT)
        .readTimeout(CALL_TIMEOUT)
        .writeTimeout(CALL_TIMEOUT)
        .build();
    protected final RepeatThrottle throttle = new RepeatThrottle(5000);

    protected Logger logger() {
        return ServiceModule.get().logger();
    }

    protected void queueStart() {
        new Thread(this::start).start();
    }

    protected void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                long start = System.currentTimeMillis();
                this.daemon();
                this.updateLastQuery();
                long timeTaken = System.currentTimeMillis() - start;
                long sleep = throttle.getSleepBuffer(normalInterval() - timeTaken);
                logger().info("%s sleeping for %d millis".formatted(name(), sleep));
                //noinspection BusyWait
                Thread.sleep(sleep);
            } catch (Exception e) {
                throttle.incrementError();
                logger().error("", e);
            }
        }
    }

    protected abstract String name();

    protected abstract void updateLastQuery();


    protected void daemon() {
        WynnResponse<Res> response;
        try {
            response = this.call();
        } catch (Exception e) {
            throttle.incrementError();
            logger().error("", e);
            return;
        }
        try {
            boolean success = this.acceptResponse(response);
            if (!success) {
                String responseJson = new Gson().toJson(response.data());
                String errorMsg = "Wynn gave a bad response in %s. Returned %s".formatted(this.getClass(), responseJson);
                throw new IllegalArgumentException(errorMsg);
            }
        } catch (Exception e) {
            throttle.incrementError();
            logger().error("", e);
        }
    }

    /**
     * @param response the response received from the WynnApi
     * @return true if response is valid and successful
     */
    protected abstract boolean acceptResponse(WynnResponse<Res> response);

    protected WynnResponse<Res> call() throws Exception {
        Call call = http.newCall(request().build());
        try (Response response = call.execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == Status.TOO_MANY_REQUESTS) {
                    logger().warn("Rate limit reached: %d error(s) in a row".formatted(throttle.getErrorCount()));
                } else {
                    ResponseBody body = response.body();
                    String message = body.string();
                    logger().error("Response code: %d, Body: %s".formatted(response.code(), message));
                }
                return WynnResponse.createError(response);
            }
            return WynnResponse.createSuccess(response, deserialize(response));
        }
    }

    protected abstract Res deserialize(Response response);


    protected abstract Request.Builder request();

    protected abstract long normalInterval();
}
