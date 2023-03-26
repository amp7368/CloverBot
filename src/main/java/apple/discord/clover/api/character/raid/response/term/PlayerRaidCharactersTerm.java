package apple.discord.clover.api.character.raid.response.term;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRaidCharactersTerm {

    public Map<UUID, RaidDelta> characters = new HashMap<>();

    public void setCharacter(UUID characterId, RaidDelta raidDelta) {
        this.characters.put(characterId, raidDelta);
    }
}
