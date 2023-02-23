package apple.discord.clover.database.guild;

import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "guild")
public class DGuild extends Model {

    @Id
    public String name;
    @Column
    public String tag;
    @Column
    public Timestamp created;

    public DGuild(String name, String tag, Instant created) {
        this.name = name;
        this.tag = tag;
        this.created = Timestamp.from(created);
    }
}
