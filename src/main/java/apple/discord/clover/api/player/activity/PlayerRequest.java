package apple.discord.clover.api.player.activity;

import apple.discord.clover.api.base.TimeResolution;
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

}
