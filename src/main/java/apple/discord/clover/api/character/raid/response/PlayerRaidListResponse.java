package apple.discord.clover.api.character.raid.response;

import apple.discord.clover.api.character.raid.response.snapshot.PlayerRaidSnapshot;
import apple.discord.clover.api.character.raid.response.term.PlayerRaidTerm;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PlayerRaidListResponse {

    public Instant requestedStart;
    public Instant requestedEnd;

    public List<PlayerRaidTerm> terms = new ArrayList<>();
    public PlayerRaidSnapshot startingSnapshot;
    public PlayerRaidSnapshot endingSnapshot;

    public PlayerRaidListResponse(Instant start, Instant end) {
        this.requestedStart = start;
        this.requestedEnd = end;
    }

    public void setTerms(List<PlayerRaidTerm> terms) {
        this.terms = terms;
    }

    public void setStart(PlayerRaidSnapshot start) {
        this.startingSnapshot = start;
    }

    public void setLast(PlayerRaidSnapshot end) {
        this.endingSnapshot = end;
    }
}
