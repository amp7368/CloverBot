package apple.discord.clover.database.activity.run;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "raid_run")
public class DRaidRun extends DSessionRunBase {

    /**
     * the value at the end of this session
     */
    @Column
    private int snapshotRuns;
    /**
     * the change in value since the start of this session
     */
    @Column
    private int deltaRuns;

}
