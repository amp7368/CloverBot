package apple.discord.clover.database.primitive;

import java.math.BigInteger;
import java.util.function.Function;
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

    public static <T> IncrementalBigInt create(T last, Function<T, IncrementalBigInt> fn, BigInteger next) {
        return new IncrementalBigInt(last == null ? null : fn.apply(last), next);
    }

    public static <T> IncrementalBigInt create(T last, Function<T, IncrementalBigInt> fn, long next) {
        return create(last, fn, BigInteger.valueOf(next));
    }
}
