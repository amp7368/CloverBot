package apple.discord.clover.database.activity.run;

import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.player.LastSessionInvalidException;
import apple.discord.clover.database.primitive.IncrementalFloat;
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
    private IncrementalFloat level;

    public DLevelupRun(String name, ProfessionLevel level, DCharacter character, @Nullable DCharacter lastCharacter)
        throws LastSessionInvalidException {
        super(name, character);
        float snapshot = level.full();
        if (lastCharacter == null) {
            this.level = new IncrementalFloat(null, snapshot);
            return;
        }
        DLevelupRun lastLevelUp = lastCharacter.getLevelup(name, character.characterId);
        if (lastLevelUp == null) {
            throw new LastSessionInvalidException(
                "Last Session Invalid: %s - lastCharacter_id=%s".formatted(name, lastCharacter.characterId));
        }
        this.level = new IncrementalFloat(lastLevelUp.level, snapshot);
    }
}
