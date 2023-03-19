package apple.discord.clover.database.activity.blacklist;

import apple.discord.clover.database.activity.partial.DLoginQueue;
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
@Table(name = "blacklist")
public class DBlacklist extends Model {

    @Id
    public UUID uuid;
    @OneToOne(mappedBy = "blacklist")
    public DLoginQueue login;
    @Column
    public int failure = 0;
    @Column
    public int success = 0;
    public Timestamp lastFailure = Timestamp.from(Instant.now());

    public DBlacklist(DLoginQueue login) {
        this.login = login;
    }
}
