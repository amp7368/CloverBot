package apple.discord.clover.wynncraft.run;

import discord.util.dcf.util.TimeMillis;

public enum WynncraftRateType {
    PLAYER_STATS(200, 275, -5),
    GUILD_STATS(30, 50, 0),
    GUILD_LIST(1, 5, 5),
    SERVER_LIST(10, 20, 5);
    public static int MAX_CALLS = 275;
    public static long TIME = TimeMillis.minToMillis(5);

    private final int reservedCalls;
    private final int maxCalls;
    private final int priority;

    WynncraftRateType(int reservedCalls, int maxCalls, int priority) {
        this.reservedCalls = reservedCalls;
        this.maxCalls = maxCalls;
        this.priority = priority;
    }

    public int priority() {
        return priority;
    }
}
