package apple.discord.clover.api.character.raid.response.term;

public class RaidDelta {

    public int delta;

    public RaidDelta(int delta) {
        this.delta = delta;
    }

    public void add(RaidDelta raidDelta) {
        this.delta += raidDelta.delta;
    }
}
