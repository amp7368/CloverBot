package apple.discord.clover.api.character.raid.response.snapshot;

import apple.discord.clover.api.character.raid.response.RaidSnapshot;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlayerRaidSnapshot {

    public Instant retrieved;

    public Map<String, PlayerRaidCharactersSnapshot> raids = new HashMap<>();

    public RaidSnapshot total = new RaidSnapshot(0);

    public PlayerRaidSnapshot(Instant retrieved) {
        this.retrieved = retrieved;
    }

    public PlayerRaidCharactersSnapshot getOrCreateRaid(String raid) {
        return raids.computeIfAbsent(raid, (k) -> new PlayerRaidCharactersSnapshot());
    }

    public void add(RaidSnapshot snapshot) {
        this.total.add(snapshot);
    }
}
