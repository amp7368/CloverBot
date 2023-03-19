package apple.discord.clover.database.primitive;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IncrementalBigInt {

    /**
     * the value at the end of this session
     */
    @Column
    public BigInteger snapshot;
    /**
     * The change in value from the last known value
     */
    @Column
    public BigInteger delta;

    public IncrementalBigInt(IncrementalBigInt last, BigInteger next) {
        this.snapshot = next;
        this.delta = this.snapshot.subtract(last == null ? BigInteger.ZERO : last.snapshot);
    }
}
