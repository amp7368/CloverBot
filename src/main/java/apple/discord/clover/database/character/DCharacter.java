package apple.discord.clover.database.character;

import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.run.DDungeonRun;
import apple.discord.clover.database.activity.run.DLevelupRun;
import apple.discord.clover.database.activity.run.DRaidRun;
import apple.discord.clover.database.activity.run.DSessionRunBase;
import apple.discord.clover.wynncraft.stats.player.character.WynnPlayerCharacter;
import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeon;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaid;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import io.ebean.Model;
import io.ebean.annotation.Index;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Index(columnNames = {"character_id", "session_id"})
@Table(name = "player_character")
public class DCharacter extends Model {

    @Id
    public UUID id;
    @Column
    public UUID character_id;

    @ManyToOne
    public DPlaySession session;

    @Column
    public String type;
    @OneToMany
    public List<DLevelupRun> levelRuns;
    @OneToMany
    public List<DDungeonRun> dungeonRuns;
    @OneToMany
    public List<DRaidRun> raidRuns;

    public DCharacter(UUID character_id, DPlaySession session, WynnPlayerCharacter character, DPlaySession lastSession) {
        this.character_id = character_id;
        this.session = session;
        this.type = character.type;
    }

    private static <T extends DSessionRunBase> T getRun(List<T> runs, String name, UUID character) {
        for (T run : runs) {
            if (run.getName().equals(name) && run.getCharacter().character_id.equals(character)) {
                return run;
            }
        }
        return null;
    }

    public void addRuns(WynnPlayerCharacter character, DPlaySession lastSession) {
        DCharacter lastCharacter = lastSession == null ? null : lastSession.getCharacter(this.character_id);
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


}
