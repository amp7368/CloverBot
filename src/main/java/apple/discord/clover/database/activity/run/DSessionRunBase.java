package apple.discord.clover.database.activity.run;

import apple.discord.clover.database.activity.DPlaySession;
import io.ebean.Model;
import io.ebean.annotation.Identity;
import io.ebean.annotation.IdentityType;
import io.ebean.annotation.Index;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Index(columnNames = {"session_session_id", "name"})
public abstract class DSessionRunBase extends Model {

    @Id
    @Identity(type = IdentityType.IDENTITY, start = 1000)
    public long runId;
    @ManyToOne
    @Column
    private DPlaySession session;

    @Column
    private String name;

    public DPlaySession getSession() {
        return session;
    }

    public String getName() {
        return name;
    }
}
