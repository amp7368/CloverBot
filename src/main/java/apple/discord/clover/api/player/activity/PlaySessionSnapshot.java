package apple.discord.clover.api.player.activity;

import apple.discord.clover.database.activity.DPlaySession;

public class PlaySessionSnapshot {

    public long playtime;
    public long combat;
    public long itemsIdentified;
    public long mobsKilled;
    public int totalProfLevel;

    public PlaySessionSnapshot(DPlaySession first, boolean isBeforeSnapshot) {
        if (isBeforeSnapshot) {
            this.playtime = first.playtime.beforeSnapshot().longValueExact();
            this.combat = first.playtime.beforeSnapshot().longValueExact();
            this.itemsIdentified = first.itemsIdentified.beforeSnapshot().longValueExact();
            this.mobsKilled = first.mobsKilled.beforeSnapshot().longValueExact();
            this.totalProfLevel = first.profLevel.beforeSnapshot();
        } else {
            this.playtime = first.playtime.snapshot.longValueExact();
            this.combat = first.playtime.snapshot.longValueExact();
            this.itemsIdentified = first.itemsIdentified.snapshot.longValueExact();
            this.mobsKilled = first.mobsKilled.snapshot.longValueExact();
            this.totalProfLevel = first.profLevel.snapshot;
        }
    }
}
