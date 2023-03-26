package apple.discord.clover.database.primitive;

import java.util.function.Function;
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

    public static <T> IncrementalInt create(T last, Function<T, IncrementalInt> fn, int next) {
        return new IncrementalInt(last == null ? null : fn.apply(last), next);
    }

    public int beforeSnapshot() {
        return this.snapshot - this.delta;
    }
}
