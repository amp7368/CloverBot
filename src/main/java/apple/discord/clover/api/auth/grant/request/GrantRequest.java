package apple.discord.clover.api.auth.grant.request;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Constraint;
import apple.discord.clover.api.base.validate.AppValidator;
import java.util.List;

public class GrantRequest {

    public static final AppValidator<GrantRequest> VALIDATOR = new AppValidator<>(
        List.of(GrantRequest::usernameValidation, GrantRequest::permissionValidation));
    public String username;
    public String[] roles;

    private static ValidatorBuilder<GrantRequest> usernameValidation(ValidatorBuilder<GrantRequest> validator) {
        return validator.constraintOnObject(req -> req.username, "username", Constraint::notNull);
    }

    private static ValidatorBuilder<GrantRequest> permissionValidation(ValidatorBuilder<GrantRequest> validator) {
        return validator._objectArray(req -> req.roles, "roles", c -> c.notNull().notEmpty());
    }
}
