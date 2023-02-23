package apple.discord.clover.database.activity.run;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "levelup_run")
public class DLevelupRun extends DSessionRunBase {

    /**
     * the value at the end of this session
     */
    @Column
    private float snapshotLevel;
    /**
     * the change in value since the start of this session
     */
    @Column
    private float deltaLevel;
}
