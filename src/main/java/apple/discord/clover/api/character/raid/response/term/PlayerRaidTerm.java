package apple.discord.clover.api.character.raid.response.term;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlayerRaidTerm {

    public Instant retrieved;

    public Map<String, PlayerRaidCharactersTerm> raids = new HashMap<>();

    public PlayerRaidTerm(Instant retrieved) {
        this.retrieved = retrieved;
    }

    public PlayerRaidCharactersTerm getOrCreateRaid(String raid) {
        return raids.computeIfAbsent(raid, r -> new PlayerRaidCharactersTerm());
    }
}
