package apple.discord.clover.api.player.activity;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.database.query.PlayerTermsQuery;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Base64;

public class PlayerController extends ApiController {

    @Override
    public void register(Javalin app) {
        app.get(this.path("/player"), this::loginStreak);
    }

    public void loginStreak(Context ctx) {
        String queryParam = new String(Base64.getUrlDecoder().decode(ctx.queryParam("search")));
        PlayerRequest request = gson().fromJson(queryParam, PlayerRequest.class);
        PlayerResponse response = PlayerTermsQuery.queryPlayerTerms(request);
        ctx.json(stringify(response));
    }
}
