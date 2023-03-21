package apple.discord.clover.api.player.character;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.player.activity.PlayerRequest;
import apple.discord.clover.database.query.character.CharacterTermsQuery;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.Base64;

public class CharacterController extends ApiController {

    public CharacterController() {
        super("/character");
    }

    @Override
    public void register(Javalin app) {
        app.get(path("/term"), this::characterTerms);
    }

    private void characterTerms(Context ctx) {
        String queryParam = new String(Base64.getUrlDecoder().decode(ctx.queryParam("search")));
        PlayerRequest request = gson().fromJson(queryParam, PlayerRequest.class);
        CharacterStatsListResponse response = CharacterTermsQuery.queryCharacterTerms(request);
        ctx.json(stringify(response));
    }
}
