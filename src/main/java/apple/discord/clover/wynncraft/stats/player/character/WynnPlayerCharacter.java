package apple.discord.clover.wynncraft.stats.player.character;


import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeons;
import apple.discord.clover.wynncraft.stats.player.character.quest.WynnPlayerQuests;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaids;

public class WynnPlayerCharacter {

    public String type;
    public int level;
    public int itemsIdentified;
    public int mobsKilled;
    public int blocksWalked;
    public int logins;
    public int deaths;
    public long playtime;
    public WynnPlayerClassGameMode gamemode;
    public WynnPlayerClassProfessions professions;
    public WynnPlayerDungeons dungeons;
    public WynnPlayerRaids raids;
    public WynnPlayerQuests quests;

}
