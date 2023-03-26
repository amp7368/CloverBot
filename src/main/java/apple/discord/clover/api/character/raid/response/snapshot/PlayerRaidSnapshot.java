package apple.discord.clover.api.character.raid.response.snapshot;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlayerRaidSnapshot {

    public Instant retrieved;

    public Map<String, PlayerRaidCharactersSnapshot> terms = new HashMap<>();

    public PlayerRaidSnapshot(Instant retrieved) {
        this.retrieved = retrieved;
    }

    public PlayerRaidCharactersSnapshot getOrCreateRaid(String raid) {
        return terms.computeIfAbsent(raid, (k) -> new PlayerRaidCharactersSnapshot());
    }
}
