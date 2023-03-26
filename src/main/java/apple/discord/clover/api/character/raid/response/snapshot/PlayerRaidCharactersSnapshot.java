package apple.discord.clover.api.character.raid.response.snapshot;

import apple.discord.clover.api.character.raid.response.RaidSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRaidCharactersSnapshot {

    public Map<UUID, RaidSnapshot> characters = new HashMap<>();

    public void setCharacter(UUID characterId, RaidSnapshot raidSnapshot) {
        this.characters.put(characterId, raidSnapshot);
    }

}
