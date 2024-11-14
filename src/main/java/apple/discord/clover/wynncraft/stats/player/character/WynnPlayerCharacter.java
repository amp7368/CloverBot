package apple.discord.clover.wynncraft.stats.player.character;


import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeon;
import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeons;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaid;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaids;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import java.util.List;
import java.util.Map;

public class WynnPlayerCharacter {

    public String reskinned;
    public String type;
    public int level;
    public float xpPercent;
    public int wars;
    public double playtime;
    public int mobsKilled;
    public int chestsFound;
    public long blocksWalked;
    public int itemsIdentified;
    public int logins;
    public int deaths;
    public int discoveries;
    public Map<String, Integer> skillPoints;
    public Map<String, ProfessionLevel> professions;
    public WynnPlayerDungeons dungeons;
    public WynnPlayerRaids raids;

    public String[] gamemode;
    public String[] quests;

    public List<WynnPlayerDungeon> dungeons() {
        if (dungeons == null) return List.of();
        else return dungeons.dungeons();
    }

    public ProfessionLevel combatLevel() {
        return new ProfessionLevel(this.level, this.xpPercent);
    }

    public List<WynnPlayerRaid> raids() {
        if (raids == null) return List.of();
        else return raids.raids();
    }

    public long playtime() {
        return (long) (playtime * 60);
    }

    public String getTypeReskinned() {
        if (this.reskinned != null) return reskinned;
        return type;
    }
}
