package apple.discord.clover.api.player.terms.request;

import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.api.base.request.TermRequest;
import apple.discord.clover.api.base.validate.AppValidator;
import java.util.List;

public class PlayerTermsRequest extends TermRequest implements HasPlayerRequest {

    public static AppValidator<PlayerTermsRequest> VALIDATOR = new AppValidator<>(
        List.of(HasPlayerRequest::hasPlayerValidator, TermRequest::termValidator));
    private String player;
    private transient PlayerNameOrUUID nameOrUUID = new PlayerNameOrUUID();

    @Override
    public PlayerNameOrUUID getPlayer() {
        return nameOrUUID;
    }

    @Override
    public String getPlayerString() {
        return player;
    }
}
