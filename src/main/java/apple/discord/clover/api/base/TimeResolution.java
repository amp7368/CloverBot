package apple.discord.clover.api.base;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public enum TimeResolution {
    HOUR(ChronoUnit.HOURS),
    DAY(ChronoUnit.DAYS),
    WEEK(ChronoUnit.WEEKS),
    MONTH(ChronoUnit.MONTHS);

    private final TemporalUnit unit;

    TimeResolution(TemporalUnit unit) {
        this.unit = unit;
    }

    public TemporalUnit unit() {
        return unit;
    }

    public String sql() {
        return name();
    }
}
