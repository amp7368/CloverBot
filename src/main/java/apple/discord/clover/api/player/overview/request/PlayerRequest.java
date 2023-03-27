package apple.discord.clover.api.player.overview.request;

import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.TermRequest;
import apple.discord.clover.api.base.validate.AppValidator;
import java.util.List;
import java.util.UUID;

public class PlayerRequest extends TermRequest implements HasPlayerRequest {

    public static AppValidator<PlayerRequest> VALIDATOR = new AppValidator<>(
        List.of(HasPlayerRequest::hasPlayerValidator, TermRequest::termValidator));
    public UUID player;

    @Override
    public UUID getPlayer() {
        return player;
    }
}
