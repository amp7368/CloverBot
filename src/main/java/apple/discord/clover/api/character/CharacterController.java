package apple.discord.clover.api.character;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.character.request.CharacterRequest;
import apple.discord.clover.api.character.response.CharacterStatsListResponse;
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
        CharacterRequest request = gson().fromJson(queryParam, CharacterRequest.class);
        CharacterStatsListResponse response = CharacterTermsQuery.queryCharacterTerms(request);
        ctx.json(stringify(response));
    }
}
