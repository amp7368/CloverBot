package apple.discord.clover.api.character.raid.response;

public class RaidSnapshot {

    public int raidCount;

    public RaidSnapshot(int raidCount) {
        this.raidCount = raidCount;
    }

    public void add(RaidSnapshot snapshot) {
        this.raidCount += snapshot.raidCount;
    }
}
