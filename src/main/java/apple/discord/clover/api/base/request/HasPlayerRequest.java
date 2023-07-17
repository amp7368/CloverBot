package apple.discord.clover.api.base.request;

import am.ik.yavi.builder.ValidatorBuilder;
import apple.discord.clover.database.player.PlayerStorage;
import java.util.UUID;

public interface HasPlayerRequest {

    static <T extends HasPlayerRequest> ValidatorBuilder<T> hasPlayerValidator(ValidatorBuilder<T> validator) {
        return validator.constraint(HasPlayerRequest::getPlayerString, "player", c -> c.notNull().notBlank());
    }

    PlayerNameOrUUID getPlayer();

    String getPlayerString();

    default PlayerNameOrUUID fetchPlayer() {
        String player = this.getPlayerString();
        try {
            UUID uuid = UUID.fromString(player);
            return getPlayer().set(uuid, PlayerStorage.findPlayerName(uuid));
        } catch (IllegalArgumentException e) {
            return getPlayer().set(PlayerStorage.findPlayerId(player), player);
        }
    }
}
