package apple.discord.clover.api;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.character.CharacterController;
import apple.discord.clover.api.meta.ping.PingController;
import apple.discord.clover.api.player.overview.PlayerController;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import io.javalin.Javalin;
import io.javalin.json.JavalinGson;
import java.util.List;

public class ApiModule extends AppleModule {

    private static ApiModule instance;
    private Javalin app;

    public ApiModule() {
        instance = this;
    }

    public static ApiModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        app = Javalin.create(cfg -> {
            ApiConfig.get().commonConfig(cfg);
            cfg.jsonMapper(new JavalinGson(ApiController.apiGson()));
        });

        registerControllers(app);

        app.start(getPort());
    }

    private void registerControllers(Javalin app) {
        new PingController().register(app);
        new PlayerController().register(app);
        new CharacterController().register(app);
    }

    private int getPort() {
        return ApiConfig.get().getPort();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(ApiConfig.class, "Api.config"));
    }

    @Override
    public String getName() {
        return "Api";
    }
}
