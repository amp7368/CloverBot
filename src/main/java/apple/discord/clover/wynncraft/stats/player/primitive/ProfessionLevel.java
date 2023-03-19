package apple.discord.clover.wynncraft.stats.player.primitive;

public class ProfessionLevel {

    public int level;
    public float xp;

    public ProfessionLevel() {
    }

    public ProfessionLevel(int level, float xp) {
        this.level = level;
        this.xp = xp;
    }

    public boolean isThisGreater(ProfessionLevel other) {
        if (other == null) return true;
        if (this.level > other.level) return true;
        else if (this.level == other.level) return this.xp > other.xp;
        return false;
    }

    public float full() {
        return this.level + this.xp / 100;
    }
}
