package apple.discord.clover.api.player;

import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.api.player.request.PlayerRequest;
import apple.discord.clover.api.player.stats.response.PlayerStatsResponse;
import apple.discord.clover.api.player.terms.request.PlayerTermsRequest;
import apple.discord.clover.api.player.terms.response.PlayerTermsResponse;
import apple.discord.clover.api.player.uuid.many.response.PlayerManyUUIDResponse;
import apple.discord.clover.api.player.uuid.one.response.PlayerOneUUIDResponse;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.player.PlayerStorage;
import apple.discord.clover.database.query.player.PlayerStatsQuery;
import apple.discord.clover.database.query.player.PlayerTermsQuery;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class PlayerController extends ApiController {

    private static final int LIKE_PLAYER_LIMIT = 50;

    public PlayerController() {
        super("/player");
    }

    @Override
    public void register(Javalin app) {
        app.get(this.path("/stats/{player}"), this::stats);
        app.get(this.path("/uuid/one/{player}"), this::uuid);
        app.get(this.path("/uuid/many/{player}"), this::uuidMany);
        app.post(this.path("/term"), this::term);
    }


    private void stats(Context ctx) {
        PlayerRequest request = new PlayerRequest();
        this.getPlayerFromPath(ctx, request::setPlayer);
        request.fetchPlayer().checkNotNull();
        PlayerStatsResponse response = PlayerStatsQuery.queryPlayerStats(request);
        ctx.json(response);
    }


    private void term(Context ctx) {
        PlayerTermsRequest request = this.checkBodyAndGet(ctx, PlayerTermsRequest.class);
        this.checkErrors(ctx, PlayerTermsRequest.VALIDATOR.validator().validate(request));
        request.fetchPlayer().checkNotNull();
        PlayerTermsResponse response = PlayerTermsQuery.queryPlayerTerms(request);
        ctx.json(response);
    }


    private void uuid(Context ctx) {
        PlayerRequest request = new PlayerRequest();
        this.getPlayerFromPath(ctx, request::setPlayer);
        request.fetchPlayer().checkNotNull();
        PlayerOneUUIDResponse response = new PlayerOneUUIDResponse(request.getPlayer());
        ctx.json(response);
    }


    private void uuidMany(Context ctx) {
        String player = this.getPlayerFromPath(ctx);
        List<DPlayer> response = PlayerStorage.findLikePlayer(player, LIKE_PLAYER_LIMIT);
        ctx.json(new PlayerManyUUIDResponse(response));
    }

    private void getPlayerFromPath(Context ctx, Consumer<String> setPlayer) {
        String param = getPlayerFromPath(ctx);
        setPlayer.accept(param);
    }

    @NotNull
    private String getPlayerFromPath(Context ctx) {
        return ctx.pathParam("player");
    }
}
