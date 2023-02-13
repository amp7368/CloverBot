package apple.discord.clover.wynncraft.player;


import discord.util.dcf.util.TimeMillis;
import java.util.UUID;

public class WynnInactivePlayer {

    private final UUID uuid;
    private final String name;
    private final long millisInactive;
    private final long timeChecked;

    public WynnInactivePlayer(WynnPlayer player) {
        this.uuid = player.uuid;
        this.name = player.username;
        this.millisInactive = player.getInactivityMillis();
        this.timeChecked = player.getTimeRetrieved();
    }

    public UUID getUUID() {
        return uuid;
    }

    public long getMillisInactive() {
        return millisInactive;
    }

    public long getTimeChecked() {
        return timeChecked;
    }

    public int getPossibleDaysInactive() {
        return (int) ((System.currentTimeMillis() - timeChecked + millisInactive) / TimeMillis.DAY);
    }

    public int getDaysInactive() {
        return (int) (millisInactive / TimeMillis.DAY);
    }

    public String getName() {
        return name;
    }
}
