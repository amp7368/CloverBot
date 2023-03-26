package apple.discord.clover.api.character.raid.response.term;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRaidCharactersTerm {

    public Map<UUID, RaidDelta> characters = new HashMap<>();
    public RaidDelta total = new RaidDelta(0);

    public void setCharacter(UUID characterId, RaidDelta raidDelta) {
        this.total.add(raidDelta);
        this.characters.put(characterId, raidDelta);
    }
}
