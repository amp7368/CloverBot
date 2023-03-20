package apple.discord.clover.api.player.activity;

import apple.discord.clover.api.base.ApiController;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Base64;

public class PlaytimeController extends ApiController {

    @Override
    public void register(Javalin app) {
    }

    public void loginStreak(Context ctx) {
        Base64.getDecoder().decode(ctx.queryParam("search"));
//        new QDPlaySession().where().player.uuid.eq("")
    }
}
