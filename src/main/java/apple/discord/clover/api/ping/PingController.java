package apple.discord.clover.api.ping;

import apple.discord.clover.api.base.ApiController;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class PingController extends ApiController {

    @Override
    public void register(Javalin app) {
        app.get("/ping", this::ping);
    }

    public void ping(Context ctx) {
        ctx.result("Pong! :D");
    }
}
