package apple.discord.clover.wynncraft.response;

import java.math.BigInteger;

public class RepeatThrottle {


    private int errors = 0;

    private final BigInteger failureBuffer;

    public RepeatThrottle(int bufferMillis) {
        this.failureBuffer = BigInteger.valueOf(bufferMillis);
    }

    public void incrementError() {
        this.errors++;
    }

    public boolean doSleepBuffer() throws InterruptedException {
        if (errors == 0) return false;
        long sleep = failureBuffer.pow(errors).longValue();
        Thread.sleep(sleep);
        return true;
    }

    public void incrementSuccess() {
        this.errors = 0;
    }

    public int getErrorCount() {
        return this.errors;
    }
}
