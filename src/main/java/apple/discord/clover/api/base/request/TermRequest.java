package apple.discord.clover.api.base.request;

import java.sql.Timestamp;
import java.time.Instant;

public class TermRequest {

    public TimeResolution timeResolution;
    public Instant start;
    /**
     * The number of terms of the resolution duration requested
     */
    public int count;

    // transient
    private transient Instant startTrunc;
    private transient Instant endTrunc;

    public synchronized Instant start() {
        if (startTrunc != null) return startTrunc;
        return startTrunc = start.truncatedTo(timeResolution.unit());
    }

    public synchronized Timestamp startSql() {
        return Timestamp.from(start());
    }

    public synchronized Instant end() {
        if (endTrunc != null) return endTrunc;
        endTrunc = start.plus(count, timeResolution.unit());
        Instant now = Instant.now().truncatedTo(timeResolution.unit());
        if (now.isBefore(endTrunc)) endTrunc = now;
        return endTrunc;
    }

    public synchronized Timestamp endSql() {
        return Timestamp.from(end());
    }
}
