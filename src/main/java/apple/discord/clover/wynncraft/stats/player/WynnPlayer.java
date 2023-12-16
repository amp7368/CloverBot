package apple.discord.clover.wynncraft.stats.player;

import apple.discord.clover.database.DVersion;
import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.player.guild.GuildStorage;
import apple.discord.clover.wynncraft.stats.player.character.WynnPlayerCharacter;
import apple.discord.clover.wynncraft.stats.player.global.WynnPlayerGlobalData;
import apple.discord.clover.wynncraft.stats.player.guild.WynnPlayerGuildData;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import apple.discord.clover.wynncraft.stats.player.ranking.WynnPlayerRanking;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class WynnPlayer {

    public String username;
    public UUID uuid;
    public String rank;
    public Date firstJoin;
    public Date lastJoin;
    public double playtime;
    public WynnPlayerGuildData guild;
    public WynnPlayerGlobalData globalData;

    public Map<UUID, WynnPlayerCharacter> characters;
    public WynnPlayerRanking ranking;
    private Instant retrieved;
    private String version;

    public long inactivityMillis() {
        return Duration.between(Instant.now(), getLastJoin()).toMillis();
    }

    public int totalCombatLevel() {
        return characters.values().stream().mapToInt(character -> character.level).sum();
    }

    public int totalProfLevel() {
        int totalProfLevel = 0;
        for (WynnPlayerCharacter character : characters.values()) {
            for (ProfessionLevel prof : character.professions.values()) {
                totalProfLevel += prof.level;
            }
        }
        return totalProfLevel;
    }


    public DGuild dGuild() {
        if (this.guild == null) return null;
        return GuildStorage.findOrCreate(this.guild.name);
    }

    public Instant getLastJoin() {
        return this.lastJoin.toInstant();
    }

    public Instant retrieved() {
        return this.retrieved;
    }

    public void setRetrieved(Instant retrieved) {
        this.retrieved = retrieved;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public DVersion version() {
        return DVersion.version(version);
    }

    public boolean isNullData() {
        return this.username == null ||
            this.uuid == null ||
            this.lastJoin == null ||
            this.globalData == null ||
            this.characters == null ||
            this.characters.isEmpty();
    }

    public long playtime() {
        return (long) (this.playtime * 60);
    }
}
