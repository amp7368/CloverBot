package apple.discord.clover.api.player.terms.response;

import apple.discord.clover.database.activity.DPlaySession;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class PlayerTermsResponse {

    public Instant requestedStart;
    public Instant requestedEnd;

    /**
     * Sorted by date
     */
    public List<PlaySessionTerm> terms;
    public PlaySessionSnapshot startingSnapshot;
    public PlaySessionSnapshot endingSnapshot;

    public PlayerTermsResponse(Instant start, Instant end) {
        this.requestedStart = start;
        this.requestedEnd = end;
    }

    public void setTerms(List<PlaySessionTerm> terms) {
        this.terms = terms;
    }

    public void setFirst(DPlaySession first, Instant requestedStart) {
        this.startingSnapshot = new PlaySessionSnapshot(first, requestedStart.isBefore(first.retrievedTime.toInstant()));
    }

    public void setLast(DPlaySession last) {
        this.endingSnapshot = new PlaySessionSnapshot(last, false);
    }

    public long playtime() {
        return sum(snapshot -> snapshot.playtimeDelta);
    }

    public Long sum(Function<PlaySessionTerm, Long> mapping) {
        return reduce(mapping, Long::sum).orElse(0L);
    }

    public <T> Optional<T> reduce(Function<PlaySessionTerm, T> mapping, BinaryOperator<T> op) {
        return terms.stream().map(mapping).reduce(op);
    }

}
