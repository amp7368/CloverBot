package apple.discord.clover.api.character.response;

import apple.discord.clover.database.character.DCharacter;

public class CharacterSnapshot {

    public long playtime;
    public int itemsIdentified;
    public int mobsKilled;
    public long blocksWalked;
    public int logins;
    public int deaths;

    public CharacterSnapshot(DCharacter first, boolean isBeforeSnapshot) {
        if (isBeforeSnapshot) {
            this.playtime = first.playtime.beforeSnapshot().longValueExact();
            this.itemsIdentified = first.itemsIdentified.beforeSnapshot();
            this.mobsKilled = first.mobsKilled.beforeSnapshot();
            this.blocksWalked = first.blocksWalked.beforeSnapshot().longValueExact();
            this.logins = first.logins.beforeSnapshot();
            this.deaths = first.deaths.beforeSnapshot();
        } else {
            this.playtime = first.playtime.snapshot.longValueExact();
            this.itemsIdentified = first.itemsIdentified.snapshot;
            this.mobsKilled = first.mobsKilled.snapshot;
            this.blocksWalked = first.blocksWalked.snapshot.longValueExact();
            this.logins = first.logins.snapshot;
            this.deaths = first.deaths.snapshot;
        }
    }
}
