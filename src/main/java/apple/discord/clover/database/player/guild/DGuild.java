package apple.discord.clover.database.player.guild;

import apple.discord.clover.api.base.BaseEntity;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "guild")
public class DGuild extends BaseEntity {

    @Id
    private String name;
    @Column
    private String tag;
    @Column
    private Timestamp created;

    public DGuild(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Timestamp getCreated() {
        return created;
    }

    public DGuild setCreated(Timestamp created) {
        this.created = created;
        return this;
    }
}
