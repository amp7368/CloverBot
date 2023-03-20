package apple.discord.clover.database.activity.run;

import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.wynncraft.stats.player.character.raid.WynnPlayerRaid;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "raid_run")
public class DRaidRun extends DSessionRunBase {

    @Column
    @Embedded(prefix = "runs_")
    private IncrementalInt runs;

    public DRaidRun(WynnPlayerRaid raid, DCharacter character, @Nullable DCharacter lastCharacter) {
        super(raid.name, character);
        int snapshot = raid.completed;
        if (lastCharacter == null) {
            this.runs = new IncrementalInt(null, snapshot);
            return;
        }
        DRaidRun last = lastCharacter.getRaid(raid.name, character.character_id);
        this.runs = new IncrementalInt(last == null ? null : last.runs, snapshot);
    }
}
