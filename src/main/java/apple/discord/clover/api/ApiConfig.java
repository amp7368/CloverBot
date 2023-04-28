package apple.discord.clover.api;

import io.javalin.config.JavalinConfig;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.apache.logging.log4j.Logger;

public class ApiConfig {

    private static ApiConfig instance;
    private final int port = 80;
    public boolean cors = false;

    public ApiConfig() {
        instance = this;
    }

    public static ApiConfig get() {
        return instance;
    }

    public void commonConfig(JavalinConfig config) {
        config.routing.treatMultipleSlashesAsSingleSlash = true;
        config.routing.ignoreTrailingSlashes = true;
        config.showJavalinBanner = false;
        if (this.cors)
            config.plugins.enableCors((cors -> cors.add(CorsPluginConfig::anyHost)));
        config.requestLogger.http((ctx, time) -> {
            Logger logger = ApiModule.get().logger();
            String message = "(%s) -- %s => %d -- %fms".formatted(ctx.ip(), ctx.path(), ctx.statusCode(), time);
            if (ctx.statusCode() < 300) {
                logger.info(message);
                return;
            }
            if (ctx.statusCode() < 500) {
                logger.warn(message);
                return;
            }
            logger.error(message + ctx.result());
        });
    }

    public int getPort() {
        return this.port;
    }
}
