package apple.discord.clover.api.character.term.response;

import java.sql.Timestamp;
import java.time.Instant;

public class CharacterTerm {

    public Instant retrieved;
    public long playtimeDelta;
    public long itemsIdentifiedDelta;
    public long mobsKilledDelta;
    public long blocksWalkedDelta;
    public long loginsDelta;
    public long deathsDelta;

    public CharacterTerm setRetrieved(Timestamp retrieved) {
        this.retrieved = retrieved.toInstant();
        return this;
    }

    public CharacterTerm setPlaytimeDelta(long playtimeDelta) {
        this.playtimeDelta = playtimeDelta;
        return this;
    }

    public CharacterTerm setItemsIdentifiedDelta(long itemsIdentifiedDelta) {
        this.itemsIdentifiedDelta = itemsIdentifiedDelta;
        return this;
    }

    public CharacterTerm setMobsKilledDelta(long mobsKilledDelta) {
        this.mobsKilledDelta = mobsKilledDelta;
        return this;
    }

    public CharacterTerm setBlocksWalkedDelta(long blocksWalkedDelta) {
        this.blocksWalkedDelta = blocksWalkedDelta;
        return this;
    }

    public CharacterTerm setLoginsDelta(long loginsDelta) {
        this.loginsDelta = loginsDelta;
        return this;
    }

    public CharacterTerm setDeathsDelta(long deathsDelta) {
        this.deathsDelta = deathsDelta;
        return this;
    }
}
