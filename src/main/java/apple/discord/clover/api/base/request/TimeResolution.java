package apple.discord.clover.api.base.request;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;

public enum TimeResolution {
    HOUR(ChronoUnit.HOURS),
    DAY(ChronoUnit.DAYS),
    WEEK(ChronoUnit.WEEKS),
    MONTH(ChronoUnit.MONTHS);

    private final TemporalUnit unit;

    TimeResolution(TemporalUnit unit) {
        this.unit = unit;
    }

    public static String errorMessage() {
        return "{0} must be one of %s".formatted(Arrays.toString(values()));
    }

    public TemporalUnit unit() {
        return unit;
    }

    public String sql() {
        return name();
    }

    public TemporalAmount duration(int terms) {
        return this.unit().getDuration().multipliedBy(terms);
    }
}
