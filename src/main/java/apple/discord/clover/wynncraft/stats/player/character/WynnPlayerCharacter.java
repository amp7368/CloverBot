package apple.discord.clover.wynncraft.stats.player.character;


import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeons;
import apple.discord.clover.wynncraft.stats.player.character.quest.WynnPlayerQuests;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaids;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import java.util.Map;

public class WynnPlayerCharacter {

    public String type;
    public int level;
    public int itemsIdentified;
    public int mobsKilled;
    public long blocksWalked;
    public int logins;
    public int deaths;
    public long playtime;
    public WynnPlayerClassGameMode gamemode;
    public Map<String, ProfessionLevel> professions;
    public WynnPlayerDungeons dungeons;
    public WynnPlayerRaids raids;
    public WynnPlayerQuests quests;

}
