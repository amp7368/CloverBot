package apple.discord.clover.api.character.raid.response.snapshot;

import apple.discord.clover.api.character.raid.response.RaidSnapshot;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRaidCharactersSnapshot {

    public Map<UUID, RaidSnapshot> characters = new HashMap<>();
    public RaidSnapshot total = new RaidSnapshot(0);

    public void setCharacter(UUID characterId, RaidSnapshot raidSnapshot) {
        this.total.add(raidSnapshot);
        this.characters.put(characterId, raidSnapshot);
    }

}
