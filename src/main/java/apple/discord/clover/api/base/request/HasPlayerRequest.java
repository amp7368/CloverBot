package apple.discord.clover.api.base.request;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Constraint;
import java.util.UUID;

public interface HasPlayerRequest {

    static <T extends HasPlayerRequest> ValidatorBuilder<T> hasPlayerValidator(ValidatorBuilder<T> validator) {
        return validator.constraintOnObject(HasPlayerRequest::getPlayer, "player", Constraint::notNull);
    }

    UUID getPlayer();
}
