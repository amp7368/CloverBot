package apple.discord.clover.database.web.auth.authentication;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.web.auth.identity.DAuthIdentity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "authentication")
public class DAuthentication extends BaseEntity {

    @Id
    private UUID id;
    @Column
    @ManyToOne(optional = false)
    private DAuthIdentity identity;

    public DAuthentication(DAuthIdentity identity) {
        this.identity = identity;
    }

    public DAuthIdentity getIdentity() {
        return identity;
    }
}
