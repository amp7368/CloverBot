package apple.discord.clover.database.activity;

import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.wynncraft.stats.player.WynnPlayerTotalLevel;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class DPlaySessionLevel {

    @Column
    @Embedded(prefix = "combat_")
    public IncrementalInt combatLevel;
    @Column
    @Embedded(prefix = "prof_")
    public IncrementalInt profLevel;

    public DPlaySessionLevel(DPlaySessionLevel last, WynnPlayerTotalLevel currentValue) {
        int combatLevel = currentValue.combat;
        this.combatLevel = IncrementalInt.create(last, l -> l.combatLevel, combatLevel);
        int profLevel = currentValue.profession;
        this.profLevel = IncrementalInt.create(last, l -> l.profLevel, profLevel);
    }
}
