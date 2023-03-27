package apple.discord.clover.api.base.validate;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;
import java.util.ArrayList;
import java.util.List;

public class AppValidator<T> {

    private final Validator<T> validator;
    private final List<CreateValidator<T>> validatorCreators;

    public AppValidator(List<CreateValidator<T>> validatorCreators) {
        this.validatorCreators = validatorCreators;
        this.validator = joinValidators(validatorCreators);
    }

    public AppValidator(CreateValidator<T> validator, List<AppValidator<T>> otherValidators) {
        this.validatorCreators = new ArrayList<>();
        this.validatorCreators.add(validator);
        for (AppValidator<T> other : otherValidators) {
            CreateValidator<T> s = other.validatorCreators.get(0);
            this.validatorCreators.addAll(other.validatorCreators);
        }
        this.validator = joinValidators(validatorCreators);
    }

    public static <T> Validator<T> joinValidators(List<CreateValidator<T>> validatorCreators) {
        ValidatorBuilder<T> builder = ValidatorBuilder.of();
        for (CreateValidator<T> create : validatorCreators) {
            builder = create.apply(builder);
        }
        return builder.build();
    }

    public Validator<T> validator() {
        return this.validator;
    }

}
