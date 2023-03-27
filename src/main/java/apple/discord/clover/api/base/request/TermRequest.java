package apple.discord.clover.api.base.request;

import am.ik.yavi.builder.ValidatorBuilder;
import apple.discord.clover.api.base.validate.AppValidator;
import apple.discord.clover.api.base.validate.InstantBeforeNowConstraint;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class TermRequest {

    public static AppValidator<TermRequest> VALIDATOR = new AppValidator<>(List.of(TermRequest::termValidator));
    private TimeResolution timeResolution;
    private Instant start;
    /**
     * The number of terms of the resolution duration requested
     */
    private int termsAfter;
    // transient
    private transient Instant startTrunc;
    private transient Instant endTrunc;

    protected static <T extends TermRequest> ValidatorBuilder<T> termValidator(ValidatorBuilder<T> validator) {
        return validator.constraintOnObject(TermRequest::getTimeResolution, "timeResolution",
                c -> c.notNull().message(TimeResolution.errorMessage()))
            .constraint(TermRequest::getStart, "start", c -> c.notNull().predicate(InstantBeforeNowConstraint.INSTANCE))
            .constraint(TermRequest::getTermsAfter, "termsAfter", c -> c.notNull().lessThanOrEqual(150).greaterThan(0));
    }

    public synchronized Instant start() {
        if (startTrunc != null) return startTrunc;
        return startTrunc = start;
//        return startTrunc = start.truncatedTo(timeResolution.unit());
    }

    public synchronized Timestamp startSql() {
        return Timestamp.from(start());
    }

    public synchronized Instant end() {
        if (endTrunc != null) return endTrunc;
        endTrunc = start.plus(termsAfter, getTimeResolution().unit());
        Instant now = Instant.now().truncatedTo(getTimeResolution().unit());
        if (now.isBefore(endTrunc)) endTrunc = now;
        return endTrunc;
    }

    public synchronized Timestamp endSql() {
        return Timestamp.from(end());
    }

    public TimeResolution getTimeResolution() {
        return timeResolution;
    }

    protected Instant getStart() {
        return start;
    }

    protected int getTermsAfter() {
        return termsAfter;
    }
}
