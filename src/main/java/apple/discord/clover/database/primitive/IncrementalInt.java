package apple.discord.clover.database.primitive;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncrementalInt {

    /**
     * the value at the end of this session
     */
    @Column
    public int snapshot;
    /**
     * The change in value from the last known value
     */
    @Column
    public int delta;

    public IncrementalInt(IncrementalInt last, int next) {
        this.snapshot = next;
        this.delta = this.snapshot - (last == null ? 0 : last.snapshot);
    }
}
