package apple.discord.clover.api.character.term.request;

import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.api.base.request.TermRequest;
import apple.discord.clover.api.base.validate.AppValidator;
import java.util.List;

public class CharacterRequest extends TermRequest implements HasPlayerRequest {


    public static AppValidator<CharacterRequest> VALIDATOR = new AppValidator<>(
        List.of(HasPlayerRequest::hasPlayerValidator, TermRequest::termValidator));

    public String player;
    private transient PlayerNameOrUUID nameOrUUID = new PlayerNameOrUUID();

    @Override
    public PlayerNameOrUUID getPlayer() {
        return nameOrUUID;
    }

    @Override
    public String getPlayerString() {
        return this.player;
    }
}
