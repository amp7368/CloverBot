package apple.discord.clover.api.player.terms.request;

import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.api.base.request.TermRequest;
import apple.discord.clover.api.base.request.TimeResolution;
import apple.discord.clover.api.base.validate.AppValidator;
import apple.discord.clover.database.player.DPlayer;
import java.time.Instant;
import java.util.List;

public class PlayerTermsRequest extends TermRequest implements HasPlayerRequest {

    public static AppValidator<PlayerTermsRequest> VALIDATOR = new AppValidator<>(
        List.of(HasPlayerRequest::hasPlayerValidator, TermRequest::termValidator));
    private final transient PlayerNameOrUUID nameOrUUID = new PlayerNameOrUUID();
    protected String player;

    public PlayerTermsRequest(TimeResolution timeResolution, Instant start, int termsAfter, DPlayer player) {
        super(timeResolution, start, termsAfter);
        this.player = player.username;
        this.nameOrUUID.set(player.uuid, player.username);
    }

    public PlayerTermsRequest() {
    }

    @Override
    public PlayerNameOrUUID getPlayer() {
        return nameOrUUID;
    }

    @Override
    public String getPlayerString() {
        return player;
    }
}
