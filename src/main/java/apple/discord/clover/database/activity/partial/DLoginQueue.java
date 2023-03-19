package apple.discord.clover.database.activity.partial;

import apple.discord.clover.database.activity.blacklist.DBlacklist;
import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "login_queue")
public class DLoginQueue extends Model {

    @Id
    public UUID id;

    @Column(nullable = false, unique = true)
    public String player;
    @Column(nullable = false)
    public Timestamp joinTime;
    @Column
    public Timestamp leaveTime;
    @Column(nullable = false)
    public int offline = 0;
    @Column(nullable = false)
    public boolean isOnline = true;
    @Column
    @OneToOne
    public DBlacklist blacklist;

    public DLoginQueue(String player, Instant requestedAt) {
        this.joinTime = Timestamp.from(requestedAt);
        this.player = player;
    }
}
