package apple.discord.clover.api.player.terms.response;

import java.sql.Timestamp;
import java.time.Instant;

public class PlaySessionTerm {

    public Instant retrieved;
    public Long playtimeDelta;
    public Long combatDelta;
    public Long itemsIdentifiedDelta;
    public Long mobsKilledDelta;
    public Long totalProfLevelDelta;

    public PlaySessionTerm setRetrieved(Timestamp retrieved) {
        this.retrieved = retrieved.toInstant();
        return this;
    }

    public PlaySessionTerm setPlaytimeDelta(Long playtimeDelta) {
        this.playtimeDelta = playtimeDelta;
        return this;
    }

    public PlaySessionTerm setCombatDelta(Long combatDelta) {
        this.combatDelta = combatDelta;
        return this;
    }

    public PlaySessionTerm setItemsIdentifiedDelta(Long itemsIdentifiedDelta) {
        this.itemsIdentifiedDelta = itemsIdentifiedDelta;
        return this;
    }

    public PlaySessionTerm setMobsKilledDelta(Long mobsKilledDelta) {
        this.mobsKilledDelta = mobsKilledDelta;
        return this;
    }

    public PlaySessionTerm setTotalProfLevelDelta(Long totalProfLevelDelta) {
        this.totalProfLevelDelta = totalProfLevelDelta;
        return this;
    }
}
