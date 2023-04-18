package apple.discord.clover.api.player.terms.response;

import apple.discord.clover.api.base.WynnMath;
import apple.discord.clover.database.activity.DPlaySession;

public class PlaySessionSnapshot {

    public long playtime;
    public int combat;
    public long itemsIdentified;
    public long mobsKilled;
    public int totalProfLevel;

    public PlaySessionSnapshot(DPlaySession entity, boolean isBeforeSnapshot) {
        if (entity == null) return;
        if (isBeforeSnapshot) {
            this.playtime = WynnMath.playtime(entity.playtime.beforeSnapshot().longValueExact());
            this.combat = entity.combatLevel.beforeSnapshot();
            this.itemsIdentified = entity.itemsIdentified.beforeSnapshot().longValueExact();
            this.mobsKilled = entity.mobsKilled.beforeSnapshot().longValueExact();
            this.totalProfLevel = entity.profLevel.beforeSnapshot();
        } else {
            this.playtime = WynnMath.playtime(entity.playtime.snapshot.longValueExact());
            this.combat = entity.combatLevel.snapshot;
            this.itemsIdentified = entity.itemsIdentified.snapshot.longValueExact();
            this.mobsKilled = entity.mobsKilled.snapshot.longValueExact();
            this.totalProfLevel = entity.profLevel.snapshot;
        }
    }
}
