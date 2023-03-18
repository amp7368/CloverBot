package apple.discord.clover.database.activity.partial;

import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "login_queue")
public class DLoginQueue extends Model {

    @Id
    public UUID id;

    @Column(nullable = false)
    public String player;
    @Column(nullable = false)
    public Timestamp joinTime;
    @Column
    public Timestamp leaveTime;
    @Column
    public int offline = 0;
    @Column
    public boolean isOnline = true;

    public DLoginQueue(String player, Instant requestedAt) {
        this.joinTime = Timestamp.from(requestedAt);
        this.player = player;
    }
}
