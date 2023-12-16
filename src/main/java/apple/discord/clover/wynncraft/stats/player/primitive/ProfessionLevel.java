package apple.discord.clover.wynncraft.stats.player.primitive;

public class ProfessionLevel {

    public int level;
    public float xpPercent;

    public ProfessionLevel() {
    }

    public ProfessionLevel(int level, float xpPercent) {
        this.level = level;
        this.xpPercent = xpPercent;
    }

    public boolean isThisGreater(ProfessionLevel other) {
        if (other == null) return true;
        if (this.level > other.level) return true;
        else if (this.level == other.level) return this.xpPercent > other.xpPercent;
        return false;
    }

    public float full() {
        return this.level + this.xpPercent / 100;
    }
}
