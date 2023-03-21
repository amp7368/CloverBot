package apple.discord.clover.database.activity.blacklist;

import apple.discord.clover.database.activity.partial.DLoginQueue;
import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "blacklist")
public class DBlacklist extends Model {

    @Id
    public String username;
    @OneToOne(mappedBy = "blacklist")
    public DLoginQueue login;
    @Column
    public int failure = 1;
    @Column
    public int success = 0;
    public Timestamp lastFailure = Timestamp.from(Instant.now());

    public DBlacklist(DLoginQueue login) {
        this.username = login.player;
        this.login = login;
    }
}
