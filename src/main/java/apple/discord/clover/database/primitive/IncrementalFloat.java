package apple.discord.clover.database.primitive;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncrementalFloat {

    /**
     * the value at the end of this session
     */
    @Column
    public float snapshot;
    /**
     * The change in value from the last known value
     */
    @Column
    public float delta;

    public IncrementalFloat(IncrementalFloat last, float next) {
        this.snapshot = next;
        this.delta = this.snapshot - (last == null ? 0 : last.snapshot);
    }
}
