package apple.discord.clover.api.player.overview;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.player.overview.request.PlayerRequest;
import apple.discord.clover.api.player.overview.response.PlayerResponse;
import apple.discord.clover.database.query.PlayerTermsQuery;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class PlayerController extends ApiController {

    public PlayerController() {
        super("/player");
    }

    @Override
    public void register(Javalin app) {
        app.post(this.path("/term"), this::loginStreak);
    }

    public void loginStreak(Context ctx) {
        PlayerRequest request = this.checkBodyAndGet(ctx, PlayerRequest.class);
        this.checkErrors(ctx, PlayerRequest.VALIDATOR.validator().validate(request));
        PlayerResponse response = PlayerTermsQuery.queryPlayerTerms(request);
        ctx.json(response);
    }
}
