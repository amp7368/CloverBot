package apple.discord.clover.api.base.request;

import am.ik.yavi.builder.ValidatorBuilder;
import apple.discord.clover.api.base.validate.AppValidator;
import apple.discord.clover.api.base.validate.InstantBeforeNowConstraint;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class TermRequest {

    public static AppValidator<TermRequest> VALIDATOR = new AppValidator<>(List.of(TermRequest::termValidator));
    protected TimeResolution timeResolution;
    protected Instant start;
    /**
     * The number of terms of the resolution duration requested
     */
    protected int termsAfter;
    // transient
    private transient Instant startTrunc;
    private transient Instant endTrunc;

    public TermRequest(TimeResolution timeResolution, Instant start, int termsAfter) {
        this.timeResolution = timeResolution;
        this.start = start;
        this.termsAfter = termsAfter;
    }

    public TermRequest() {
    }

    protected static <T extends TermRequest> ValidatorBuilder<T> termValidator(ValidatorBuilder<T> validator) {
        return validator.constraintOnObject(TermRequest::getTimeResolution, "timeResolution",
                c -> c.notNull().message(TimeResolution.errorMessage()))
            .constraint(TermRequest::getStart, "start", c -> c.notNull().predicate(InstantBeforeNowConstraint.INSTANCE))
            .constraint(TermRequest::getTermsAfter, "termsAfter", c -> c.notNull().lessThanOrEqual(1095).greaterThan(0));
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
        endTrunc = start.plus(getTimeResolution().duration(termsAfter));
        Instant now = Instant.now();
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
