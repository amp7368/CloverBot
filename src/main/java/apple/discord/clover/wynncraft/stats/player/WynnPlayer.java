package apple.discord.clover.wynncraft.stats.player;

import apple.discord.clover.database.guild.DGuild;
import apple.discord.clover.database.guild.GuildStorage;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import apple.discord.clover.wynncraft.stats.player.character.WynnPlayerCharacter;
import apple.discord.clover.wynncraft.stats.player.global.WynnPlayerGlobalData;
import apple.discord.clover.wynncraft.stats.player.guild.WynnPlayerGuildData;
import apple.discord.clover.wynncraft.stats.player.meta.WynnPlayerMeta;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionType;
import apple.discord.clover.wynncraft.stats.player.ranking.WynnPlayerRanking;
import apple.utilities.util.FuzzyStringMatcher;
import discord.util.dcf.util.TimeMillis;
import java.util.Map;
import java.util.UUID;

public class WynnPlayer {

    private static final long TIME_TO_SAVE = TimeMillis.HOUR;
    public String username;
    public UUID uuid;
    public String rank;
    public WynnPlayerMeta meta;
    public Map<UUID, WynnPlayerCharacter> characters;
    public WynnPlayerGuildData guild;
    public WynnPlayerGlobalData global;
    public WynnPlayerRanking ranking;
    public transient long timeRetrieved;
    public transient WynnGuildMember guildMember;
    private transient FuzzyStringMatcher usernamePattern = null;
    private transient ProfessionLevel[] maxProfs = null;

    public boolean isOld() {
        return System.currentTimeMillis() - timeRetrieved > TIME_TO_SAVE;
    }

    public void addGuildMemberInfo(WynnGuildMember guildMember) {
        this.guildMember = guildMember;
    }

    public int getInactiveDays() {
        return (int) (getInactivityMillis() / TimeMillis.DAY);
    }

    public long getInactivityMillis() {
        return timeRetrieved - meta.lastJoin.getTime();
    }

    public long getTimeRetrieved() {
        return timeRetrieved;
    }

    public int hoursPlayed() {
        return (int) (this.meta.playtime / 60);
    }

    public ProfessionLevel getProf(ProfessionType professionType) {
        if (maxProfs == null) {
            maxProfs = new ProfessionLevel[ProfessionType.values().length];
            int i = 0;
            for (ProfessionType prof : ProfessionType.values()) {
                maxProfs[i++] = calculateMaxProf(prof);
            }
        }
        return maxProfs[professionType.ordinal()];
    }

    private ProfessionLevel calculateMaxProf(ProfessionType prof) {
        ProfessionLevel level = null;
        for (WynnPlayerCharacter wynnClass : characters.values()) {
            ProfessionLevel level1 = wynnClass.professions.get(prof.name());
            if (level1 == null) continue;
            if (level1.isThisGreater(level)) {
                level = level1;
            }
        }
        if (level == null) {
            level = new ProfessionLevel(0, 0);
        }
        return level;
    }

    public WynnInactivePlayer toWynnInactivePlayer() {
        return new WynnInactivePlayer(this);
    }

    public boolean nameEquals(String playerName) {
        return this.username.equalsIgnoreCase(playerName);
    }

    public boolean nameContains(String playerName) {
        if (usernamePattern == null) {
            usernamePattern = new FuzzyStringMatcher(username, FuzzyStringMatcher.Flag.CONTAINS,
                FuzzyStringMatcher.Flag.CASE_INSENSITIVE);
        }
        return usernamePattern.operationsToMatch(playerName, 0) >= 0;
    }

    public boolean nameOneCharOff(String playerName) {
        if (usernamePattern == null) {
            usernamePattern = new FuzzyStringMatcher(username, FuzzyStringMatcher.Flag.CONTAINS,
                FuzzyStringMatcher.Flag.CASE_INSENSITIVE);
        }
        return usernamePattern.operationsToMatch(playerName, 1) >= 0;
    }

    public DGuild dGuild() {
        if (this.guild.name == null) return null;
        return GuildStorage.findOrCreate(this.guild.name);
    }
}
