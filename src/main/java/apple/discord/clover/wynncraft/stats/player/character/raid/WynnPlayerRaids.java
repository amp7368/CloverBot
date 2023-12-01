package apple.discord.clover.wynncraft.stats.player.character.raid;

import java.util.List;
import java.util.Map;

public class WynnPlayerRaids {

    public int total;
    public Map<String, Integer> list;

    public List<WynnPlayerRaid> raids() {
        return list.entrySet()
            .stream()
            .map(e -> new WynnPlayerRaid(e.getKey(), e.getValue()))
            .toList();
    }
}
