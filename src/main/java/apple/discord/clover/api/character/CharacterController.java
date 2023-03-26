package apple.discord.clover.api.character;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.character.raid.request.CharacterRaidRequest;
import apple.discord.clover.api.character.raid.response.PlayerRaidListResponse;
import apple.discord.clover.api.character.request.CharacterRequest;
import apple.discord.clover.api.character.response.CharacterStatsListResponse;
import apple.discord.clover.database.query.character.CharacterQuery;
import apple.discord.clover.database.query.raid.RaidRunQuery;
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
        app.get(path("/raid"), this::characterRaid);
    }

    private void characterRaid(Context ctx) {
        String queryParam = new String(Base64.getUrlDecoder().decode(ctx.queryParam("search")));
        CharacterRaidRequest request = gson().fromJson(queryParam, CharacterRaidRequest.class);
        PlayerRaidListResponse response = RaidRunQuery.raidRunQuery(request);
        ctx.json(stringify(response));
    }

    private void characterTerms(Context ctx) {
        String queryParam = new String(Base64.getUrlDecoder().decode(ctx.queryParam("search")));
        CharacterRequest request = gson().fromJson(queryParam, CharacterRequest.class);
        CharacterStatsListResponse response = CharacterQuery.queryCharacters(request);
        ctx.json(stringify(response));
    }
}
