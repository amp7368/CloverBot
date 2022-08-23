package apple.discord.clover.wynncraft;

import apple.utilities.request.AppleRequestPriorityService;

public enum WynnRequestPriority implements AppleRequestPriorityService.AppleRequestPriority {
    IMMEDIATE(600, 750, 10),
    NOW(600, 750, 10),
    PRIMARY(600, 750, 10),
    LAZY(500, 1000, 10),
    BACKGROUND(400, 2000, 20);

    private final int requestsPerTimeUnit;
    private final int safeGuardBuffer;
    private final int failSafeGuardBuffer;

    WynnRequestPriority(int requestsPerTimeUnit, int safeGuardBuffer, int failSafeGuardBuffer) {
        this.requestsPerTimeUnit = requestsPerTimeUnit;
        this.safeGuardBuffer = safeGuardBuffer;
        this.failSafeGuardBuffer = safeGuardBuffer * failSafeGuardBuffer;
    }

    @Override
    public int getRequestsPerTimeUnit() {
        return requestsPerTimeUnit;
    }


    @Override
    public int getSafeGuardBuffer() {
        return safeGuardBuffer;
    }

    @Override
    public int getFailGuardBuffer() {
        return failSafeGuardBuffer;
    }
}
