package apple.discord.clover.wynncraft.stats.player.character.dungeon;

import java.util.List;
import java.util.Map;

public class WynnPlayerDungeons {

    public int total;
    public Map<String, Integer> list;

    public List<WynnPlayerDungeon> dungeons() {
        return list.entrySet()
            .stream()
            .map(e -> new WynnPlayerDungeon(e.getKey(), e.getValue()))
            .toList();
    }
}
