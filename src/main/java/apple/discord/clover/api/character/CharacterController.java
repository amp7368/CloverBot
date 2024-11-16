package apple.discord.clover.api.character;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.character.raid.request.CharacterRaidRequest;
import apple.discord.clover.api.character.raid.response.PlayerRaidListResponse;
import apple.discord.clover.api.character.term.request.CharacterRequest;
import apple.discord.clover.api.character.term.response.CharacterStatsListResponse;
import apple.discord.clover.database.query.character.CharacterQuery;
import apple.discord.clover.database.query.raid.RaidRunQuery;
import apple.discord.clover.database.web.auth.ApiSecurity;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class CharacterController extends ApiController {

    public CharacterController() {
        super("/character");
    }

    @Override
    public void register(Javalin app) {
        app.post(path("/term"), this::characterTerms);
        app.post(path("/raid"), this::characterRaid);
    }

    private void characterRaid(Context ctx) {
        CharacterRaidRequest request = this.checkBodyAndGet(ctx, CharacterRaidRequest.class);
        this.checkErrors(ctx, CharacterRaidRequest.VALIDATOR.validator().validate(request));
        request.fetchPlayer().checkNotNull();
        ApiSecurity.verifyPlayerDataPermission(ctx, request.getPlayer());
        PlayerRaidListResponse response = RaidRunQuery.raidRunQuery(request);
        ctx.json(response);
    }

    private void characterTerms(Context ctx) {
        CharacterRequest request = this.checkBodyAndGet(ctx, CharacterRequest.class);
        this.checkErrors(ctx, CharacterRequest.VALIDATOR.validator().validate(request));
        request.fetchPlayer().checkNotNull();
        ApiSecurity.verifyPlayerDataPermission(ctx, request.getPlayer());
        CharacterStatsListResponse response = CharacterQuery.queryCharacters(request);
        ctx.json(response);
    }
}
