package apple.discord.clover.database.activity;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.primitive.IncrementalBigInt;
import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import io.ebean.annotation.Index;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "play_session")
@UniqueConstraint(columnNames = {"player_uuid", "join_time"})
@Index(columnNames = {"player_uuid", "join_time"})
public class DPlaySession extends BaseEntity {

    @Id
    public UUID id;

    // unique
    @ManyToOne
    public DPlayer player;
    @Column
    public Timestamp joinTime;
    @Column
    public Timestamp retrievedTime;

    @ManyToOne
    public DGuild guild;

    @OneToMany(mappedBy = "session")
    public List<DCharacter> characters;

    /**
     * The duration that the player has been logged in
     *
     * @apiNote Uses Wynncraft's "minutes". There is a formula to translate this number to human "minutes"
     */
    @Column
    @Embedded(prefix = "playtime_")
    public IncrementalBigInt playtime;
    @Column
    @Embedded(prefix = "items_identified_")
    public IncrementalBigInt itemsIdentified;
    @Column
    @Embedded(prefix = "mobs_killed_")
    public IncrementalBigInt mobsKilled;

    @Column
    @Embedded(prefix = "combat_")
    public IncrementalInt combatLevel;
    @Column
    @Embedded(prefix = "prof_")
    public IncrementalInt profLevel;

    public DPlaySession(DPlayer playerInDB, DPlaySession lastSession, DLoginQueue login, WynnPlayer currentValue) {
        this.player = playerInDB;
        this.joinTime = login.joinTime;
        this.retrievedTime = Timestamp.from(Instant.ofEpochMilli(currentValue.timeRetrieved));
        this.guild = currentValue.dGuild();

        long playtime = currentValue.meta.playtime;
        this.playtime = IncrementalBigInt.create(lastSession, s -> s.playtime, playtime);
        long itemsIdentified = currentValue.global.itemsIdentified;
        this.itemsIdentified = IncrementalBigInt.create(lastSession, s -> s.itemsIdentified, itemsIdentified);
        long mobsKilled = currentValue.global.mobsKilled;
        this.mobsKilled = IncrementalBigInt.create(lastSession, s -> s.mobsKilled, mobsKilled);
        int combatLevel = currentValue.global.totalLevel.combat;
        this.combatLevel = IncrementalInt.create(lastSession, l -> l.combatLevel, combatLevel);
        int profLevel = currentValue.global.totalLevel.profession;
        this.profLevel = IncrementalInt.create(lastSession, l -> l.profLevel, profLevel);

    }

    public DCharacter getCharacter(UUID id) {
        for (DCharacter ch : this.characters) {
            if (ch.characterId.equals(id)) {
                return ch;
            }
        }
        return null;
    }

    public void addCharacter(DCharacter character) {
        if (this.characters == null) this.characters = new ArrayList<>();
        this.characters.add(character);
    }

    public List<DCharacter> getCharacters() {
        return this.characters;
    }
}
