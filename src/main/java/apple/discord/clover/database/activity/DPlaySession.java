package apple.discord.clover.database.activity;

import apple.discord.clover.database.activity.run.DDungeonRun;
import apple.discord.clover.database.activity.run.DLevelupRun;
import apple.discord.clover.database.activity.run.DRaidRun;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.guild.DGuild;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.primitive.IncrementalInt;
import io.ebean.annotation.Index;
import java.sql.Timestamp;
import java.util.List;
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
@UniqueConstraint(columnNames = {"player_id", "join_time"})
@Index(columnNames = {"player_id", "join_time"})
public class DPlaySession {

    @Id
    public long sessionId;

    // unique
    @ManyToOne
    public DPlayer player;
    @Column
    public Timestamp joinTime;

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
    public IncrementalInt playtime;

    @OneToMany(mappedBy = "session")
    public List<DCharacter> characters;
    @OneToMany(mappedBy = "session")
    public List<DLevelupRun> combatLevelRuns;
    @OneToMany(mappedBy = "session")
    public List<DDungeonRun> dungeonRuns;
    @OneToMany(mappedBy = "session")
    public List<DRaidRun> raidRuns;
}
