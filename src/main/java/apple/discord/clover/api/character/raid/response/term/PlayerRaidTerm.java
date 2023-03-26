package apple.discord.clover.api.character.raid.response.term;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlayerRaidTerm {

    public Instant retrieved;

    public Map<String, PlayerRaidCharactersTerm> terms = new HashMap<>();

    public PlayerRaidTerm(Instant retrieved) {
        this.retrieved = retrieved;
    }

    public PlayerRaidCharactersTerm getOrCreateRaid(String raid) {
        return terms.computeIfAbsent(raid, r -> new PlayerRaidCharactersTerm());
    }
}
