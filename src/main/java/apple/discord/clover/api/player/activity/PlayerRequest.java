package apple.discord.clover.api.player.activity;

import apple.discord.clover.api.base.TimeResolution;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class PlayerRequest {

    public TimeResolution timeResolution;
    public UUID player;
    public Instant start;
    /**
     * The number of terms of the resolution duration requested
     */
    public int count;
    private transient Instant startSql;
    private transient Instant endSql;

    public Instant start() {
        if (startSql != null) return startSql;
        return startSql = start.truncatedTo(timeResolution.unit());
    }

    public Timestamp startSql() {
        return Timestamp.from(start());
    }

    public Instant end() {
        if (endSql != null) return endSql;
        return endSql = start.plus(count, timeResolution.unit());
    }

    public Timestamp endSql() {
        return Timestamp.from(end());
    }
}
