package apple.discord.clover.database.activity.run;

import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.player.LastSessionInvalidException;
import apple.discord.clover.database.primitive.IncrementalFloat;
import apple.discord.clover.database.query.run.RunStorage;
import apple.discord.clover.wynncraft.stats.player.primitive.ProfessionLevel;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "levelup_run")
public class DLevelupRun extends DSessionRunBase {

    @Column
    @Embedded(prefix = "level_")
    protected IncrementalFloat level;

    public DLevelupRun(String name, ProfessionLevel level, DCharacter character, @Nullable DCharacter lastCharacter)
        throws LastSessionInvalidException {
        super(name, character);
        DLevelupRun lastLevelUp = RunStorage.findRecentLevelRun(character.characterId, name);
        IncrementalFloat lastLevel = lastLevelUp == null ? null : lastLevelUp.level;
        float snapshot = level.full();
        this.level = new IncrementalFloat(lastLevel, snapshot);
    }

    public float getDelta() {
        return this.level.delta;
    }

   
}
