package apple.discord.clover.api.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import java.time.Instant;

public abstract class ApiController {

    private final String basePath;

    public ApiController(String basePath) {
        this.basePath = version() + basePath;
    }

    public ApiController() {
        this.basePath = version();
    }

    protected String version() {
        return "/v1";
    }

    protected String path(String subPath) {
        return this.basePath + subPath;
    }

    public abstract void register(Javalin app);

    protected String stringify(Object obj) {
        return gson().toJson(obj);
    }

    protected Gson gson() {
        return new GsonBuilder().registerTypeAdapter(Instant.class, new InstantSerializer()).create();
    }
}
