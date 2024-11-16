package apple.discord.clover.database.web.auth.identity;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.web.auth.role.DAuthRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table(name = "auth_identity_role_bridge")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "identity_id"), @PrimaryKeyJoinColumn(name = "role_id")})
public class DIdentityRoleBridge extends BaseEntity {

    @ManyToOne
    @Column(nullable = false)
    public DAuthIdentity identity;
    @OneToOne
    @Column(nullable = false)
    public DAuthRole role;

    public DIdentityRoleBridge(DAuthIdentity identity, DAuthRole role) {
        this.identity = identity;
        this.role = role;
    }

    public DAuthRole getRole() {
        return this.role;
    }

    public DAuthIdentity getIdentity() {
        return identity;
    }
}
