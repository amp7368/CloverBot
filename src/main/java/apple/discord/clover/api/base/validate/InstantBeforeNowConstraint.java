package apple.discord.clover.api.base.validate;

import am.ik.yavi.core.CustomConstraint;
import java.time.Instant;
import org.jetbrains.annotations.NotNull;

public class InstantBeforeNowConstraint implements CustomConstraint<Instant> {


    public static final CustomConstraint<Instant> INSTANCE = new InstantBeforeNowConstraint();

    private InstantBeforeNowConstraint() {
    }

    @Override
    @NotNull
    public String defaultMessageFormat() {
        return "Instant value \"{0}\" must be earlier than the current time.";
    }

    @Override
    @NotNull
    public String messageKey() {
        return "instant.beforeNow";
    }

    @Override
    public boolean test(Instant instant) {
        return instant.isBefore(Instant.now());
    }
}