package apple.discord.clover.database.guild;

import apple.discord.clover.api.base.BaseEntity;
import io.ebean.annotation.DbDefault;
import io.ebean.config.dbplatform.DbDefaultValue;
import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "guild")
public class DGuild extends BaseEntity {

    @Id
    private UUID id;
    @Column
    private String name;
    @Column
    private String tag;
    @Column
    private Timestamp created;

    @Column
    @DbDefault(value = DbDefaultValue.TRUE)
    private boolean isActive = true;

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

    public boolean isActive() {
        return this.isActive;
    }

    public DGuild setInactive() {
        this.isActive = false;
        return this;
    }
}
