package apple.discord.clover.api.auth.role;

import am.ik.yavi.builder.ValidatorBuilder;
import apple.discord.clover.api.base.validate.AppValidator;
import java.util.List;

public class CreateRoleRequest {

    public static final AppValidator<CreateRoleRequest> VALIDATOR = new AppValidator<>(
        List.of(CreateRoleRequest::validateName, CreateRoleRequest::validatePermissions));
    public String name;
    public String[] permissions;

    private static ValidatorBuilder<CreateRoleRequest> validateName(ValidatorBuilder<CreateRoleRequest> validator) {
        return validator._charSequence((CreateRoleRequest req) -> req.name, "name", c -> c.notNull().notBlank());
    }

    private static ValidatorBuilder<CreateRoleRequest> validatePermissions(ValidatorBuilder<CreateRoleRequest> validator) {
        return validator._objectArray((CreateRoleRequest req) -> req.permissions, "name", c -> c.notNull().notEmpty());
    }
}
