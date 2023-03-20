package apple.discord.clover.database.activity.run;

import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.wynncraft.stats.player.character.dungeon.WynnPlayerDungeon;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dungeon_run")
public class DDungeonRun extends DSessionRunBase {

    @Column
    @Embedded(prefix = "runs_")
    public IncrementalInt runs;

    public DDungeonRun(WynnPlayerDungeon dungeon, DCharacter character, DCharacter lastCharacter) {
        super(dungeon.name, character);
        int snapshot = dungeon.completed;
        if (lastCharacter == null) {
            this.runs = new IncrementalInt(null, snapshot);
            return;
        }
        DDungeonRun last = lastCharacter.getDungeon(dungeon.name, character.character_id);
        this.runs = new IncrementalInt(last == null ? null : last.runs, dungeon.completed);
    }
}
