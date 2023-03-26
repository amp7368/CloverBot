package apple.discord.clover.database.query.raid;

import apple.discord.clover.api.character.raid.response.RaidSnapshot;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class RaidRunRaw {

    public Instant retrieved;
    public int runsDelta;
    public int runsSnapshot;
    public String raid;
    public UUID characterId;

    public RaidSnapshot runsSnapshotAt(Instant date) {
        if (retrieved.isBefore(date)) {
            return new RaidSnapshot(runsSnapshot);
        }
        return new RaidSnapshot(runsSnapshot - runsDelta);
    }

    public RaidRunRaw setRetrieved(Timestamp retrieved) {
        this.retrieved = retrieved.toInstant();
        return this;
    }

    public RaidRunRaw setRunsDelta(int runsDelta) {
        this.runsDelta = runsDelta;
        return this;
    }

    public RaidRunRaw setRunsSnapshot(int runsSnapshot) {
        this.runsSnapshot = runsSnapshot;
        return this;
    }

    public RaidRunRaw setRaid(String raid) {
        this.raid = raid;
        return this;
    }

    public RaidRunRaw setCharacterId(UUID characterId) {
        this.characterId = characterId;
        return this;
    }
}
