package apple.discord.clover.database.character;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.run.DDungeonRun;
import apple.discord.clover.database.activity.run.DLevelupRun;
import apple.discord.clover.database.activity.run.DRaidRun;
import apple.discord.clover.database.activity.run.DSessionRunBase;
import apple.discord.clover.database.player.LastSessionInvalidException;
import apple.discord.clover.database.primitive.IncrementalBigInt;
import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.wynncraft.stats.player.character.WynnPlayerCharacter;
import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeon;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaid;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import io.ebean.annotation.Index;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Index(columnNames = {"character_id", "session_id"})
@Table(name = "player_character")
public class DCharacter extends BaseEntity {

    @Id
    public UUID sku;
    @Column
    public UUID characterId;

    @ManyToOne
    public DPlaySession session;

    @OneToMany
    public List<DLevelupRun> levelRuns;
    @OneToMany
    public List<DDungeonRun> dungeonRuns;
    @OneToMany
    public List<DRaidRun> raidRuns;
    @Column
    public String type;
    @Column
    @Embedded(prefix = "items_identified_")
    public IncrementalInt itemsIdentified;
    @Column
    @Embedded(prefix = "mobs_killed_")
    public IncrementalInt mobsKilled;
    @Column
    @Embedded(prefix = "blocks_walked_")
    public IncrementalBigInt blocksWalked;
    @Column
    @Embedded(prefix = "logins_")
    public IncrementalInt logins;
    @Column
    @Embedded(prefix = "deaths_")
    public IncrementalInt deaths;
    @Column
    @Embedded(prefix = "playtime_")
    public IncrementalBigInt playtime;

    public DCharacter(UUID characterId, DPlaySession session, WynnPlayerCharacter current, DCharacter last) {
        this.characterId = characterId;
        this.session = session;
        this.type = current.type;
        this.itemsIdentified = IncrementalInt.create(last, l -> l.itemsIdentified, current.itemsIdentified);
        this.mobsKilled = IncrementalInt.create(last, l -> l.mobsKilled, current.mobsKilled);
        this.blocksWalked = IncrementalBigInt.create(last, l -> l.blocksWalked, current.blocksWalked);
        this.logins = IncrementalInt.create(last, l -> l.logins, current.logins);
        this.deaths = IncrementalInt.create(last, l -> l.deaths, current.deaths);
        this.playtime = IncrementalBigInt.create(last, l -> l.playtime, current.playtime);
    }

    private static <T extends DSessionRunBase> T getRun(List<T> runs, String name, UUID character) {
        for (T run : runs) {
            if (run.getName().equals(name) && run.getCharacter().characterId.equals(character)) {
                return run;
            }
        }
        return null;
    }

    public void addRuns(WynnPlayerCharacter character, DPlaySession lastSession) throws LastSessionInvalidException {
        DCharacter lastCharacter = lastSession == null ? null : lastSession.getCharacter(this.characterId);
        this.levelRuns = new ArrayList<>();
        for (Entry<String, ProfessionLevel> prof : character.professions.entrySet()) {
            this.levelRuns.add(new DLevelupRun(prof.getKey(), prof.getValue(), this, lastCharacter));
        }
        this.dungeonRuns = new ArrayList<>();
        for (WynnPlayerDungeon dungeon : character.dungeons.list) {
            this.dungeonRuns.add(new DDungeonRun(dungeon, this, lastCharacter));
        }
        this.raidRuns = new ArrayList<>();
        for (WynnPlayerRaid raid : character.raids.list) {
            this.raidRuns.add(new DRaidRun(raid, this, lastCharacter));
        }
    }

    public DLevelupRun getLevelup(String name, UUID character) {
        return getRun(this.levelRuns, name, character);
    }

    public DDungeonRun getDungeon(String name, UUID character) {
        return getRun(this.dungeonRuns, name, character);
    }

    public DRaidRun getRaid(String name, UUID character) {
        return getRun(this.raidRuns, name, character);
    }


    public DPlaySession getSession() {
        return this.session;
    }

    public List<DLevelupRun> getLevelRuns() {
        return levelRuns == null ? List.of() : levelRuns;
    }

    public List<DDungeonRun> getDungeonRuns() {
        return dungeonRuns == null ? List.of() : dungeonRuns;
    }

    public List<DRaidRun> getRaidRuns() {
        return raidRuns == null ? List.of() : raidRuns;
    }
}
