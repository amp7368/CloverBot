package apple.discord.clover.api.base;

import com.google.gson.GsonBuilder;
import io.javalin.Javalin;
import java.time.Instant;

public abstract class ApiController {

    private final String basePath;

    public ApiController(String basePath) {
        this.basePath = basePath;
    }

    public ApiController() {
        this.basePath = "";
    }

    protected String path(String subPath) {
        return this.basePath + subPath;
    }

    public abstract void register(Javalin app);

    protected String stringify(Object obj) {
        return new GsonBuilder().registerTypeAdapter(Instant.class, new InstantSerializer()).create().toJson(obj);
    }
}
