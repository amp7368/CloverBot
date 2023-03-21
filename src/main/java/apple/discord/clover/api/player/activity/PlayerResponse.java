package apple.discord.clover.api.player.activity;

import apple.discord.clover.database.activity.DPlaySession;
import java.time.Instant;
import java.util.List;

public class PlayerResponse {

    public Instant requestedStart;
    public Instant requestedEnd;

    /**
     * Sorted by date
     */
    public List<PlaySessionTerm> terms;
    private PlaySessionSnapshot startingSnapshot;
    private PlaySessionSnapshot endingSnapshot;

    public PlayerResponse(Instant start, Instant end) {
        this.requestedStart = start;
        this.requestedEnd = end;
    }

    public void setTerms(List<PlaySessionTerm> terms) {
        this.terms = terms;
    }

    public void setFirst(DPlaySession first) {
        this.startingSnapshot = new PlaySessionSnapshot(first, true);
    }

    public void setLast(DPlaySession last) {
        this.endingSnapshot = new PlaySessionSnapshot(last, false);
    }
}
