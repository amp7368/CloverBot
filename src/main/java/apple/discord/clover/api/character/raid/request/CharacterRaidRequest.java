package apple.discord.clover.api.character.raid.request;

import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.api.base.request.TermRequest;
import apple.discord.clover.api.base.validate.AppValidator;
import java.util.List;

public class CharacterRaidRequest extends TermRequest implements HasPlayerRequest {

    public static AppValidator<CharacterRaidRequest> VALIDATOR = new AppValidator<>(
        List.of(HasPlayerRequest::hasPlayerValidator, TermRequest::termValidator));
    private final transient PlayerNameOrUUID nameOrUUID = new PlayerNameOrUUID();
    public String player;

    @Override
    public PlayerNameOrUUID getPlayer() {
        return nameOrUUID;
    }

    @Override
    public String getPlayerString() {
        return this.player;
    }
}
