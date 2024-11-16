package apple.discord.clover.database.queue.blacklist;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.queue.partial.DLoginQueue;
import java.sql.Timestamp;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "blacklist")
public class DBlacklist extends BaseEntity {

    @Id
    public String username;
    @OneToOne
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

    public DBlacklist incrementFailure() {
        this.failure++;
        return this;
    }

    public DBlacklist incrementSuccess() {
        this.success++;
        return this;
    }

    public DLoginQueue getLogin() {
        return this.login;
    }

    public DBlacklist setLogin(DLoginQueue login) {
        this.login = login;
        return this;
    }
}
