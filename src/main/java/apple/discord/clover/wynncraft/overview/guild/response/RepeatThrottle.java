package apple.discord.clover.wynncraft.overview.guild.response;

import java.math.BigInteger;

public class RepeatThrottle {


    private final BigInteger failureBuffer;
    private int errors = 0;

    public RepeatThrottle(int bufferMillis) {
        this.failureBuffer = BigInteger.valueOf(bufferMillis);
    }

    public void incrementError() {
        this.errors++;
    }

    public long getSleepBuffer(long minSleep) {
        if (errors == 0) return Math.max(1, minSleep);
        long sleep = failureBuffer.multiply(BigInteger.TWO.pow(errors - 1)).longValue();
        return Math.max(1, Math.max(sleep, minSleep));
    }

    public void incrementSuccess() {
        this.errors = 0;
    }

    public int getErrorCount() {
        return this.errors;
    }
}
