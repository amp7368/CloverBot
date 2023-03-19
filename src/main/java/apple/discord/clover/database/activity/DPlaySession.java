package apple.discord.clover.database.activity;

import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.guild.DGuild;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.primitive.IncrementalBigInt;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import io.ebean.Model;
import io.ebean.annotation.Index;
import java.math.BigInteger;
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
public class DPlaySession extends Model {

    @Id
    public UUID id;

    // unique
    @ManyToOne
    public DPlayer player;
    @Column
    public Timestamp joinTime;
    @Column
    public Timestamp retrievedTime;

    // data
    @ManyToOne
    public DGuild guild;

    /**
     * The duration that the player has been logged in
     *
     * @apiNote Uses Wynncraft's "minutes". There is a formula to translate this number to human "minutes"
     */
    @Column
    @Embedded(prefix = "playtime")
    public IncrementalBigInt playtime;

    @OneToMany(mappedBy = "session")
    public List<DCharacter> characters;

    public DPlaySession(DPlayer playerInDB, DPlaySession lastSession, DLoginQueue login, WynnPlayer currentValue) {
        this.player = playerInDB;
        this.joinTime = login.joinTime;
        this.retrievedTime = Timestamp.from(Instant.ofEpochMilli(currentValue.timeRetrieved));
        this.guild = currentValue.dGuild();
        IncrementalBigInt lastPlaytime = lastSession == null ? null : lastSession.playtime;
        BigInteger currentPlaytime = BigInteger.valueOf(currentValue.meta.playtime);
        this.playtime = new IncrementalBigInt(lastPlaytime, currentPlaytime);
    }

    public DCharacter getCharacter(UUID id) {
        for (DCharacter ch : this.characters) {
            if (ch.character_id.equals(id)) {
                return ch;
            }
        }
        return null;
    }

    public void addCharacter(DCharacter character) {
        if (this.characters == null) this.characters = new ArrayList<>();
        this.characters.add(character);
    }
}
