package apple.discord.clover.api.base.validate;

import am.ik.yavi.builder.ValidatorBuilder;
import java.util.function.Function;

public interface CreateValidator<T> extends Function<ValidatorBuilder<T>, ValidatorBuilder<T>> {

}
