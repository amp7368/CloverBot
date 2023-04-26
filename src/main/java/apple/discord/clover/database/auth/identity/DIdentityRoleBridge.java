package apple.discord.clover.database.auth.identity;

import apple.discord.clover.database.auth.role.DAuthRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "identity"), @PrimaryKeyJoinColumn(name = "role")})
public class DIdentityRoleBridge {

    @ManyToOne
    @Column(nullable = false)
    public DAuthIdentity identity;
    @OneToOne
    @Column(nullable = false)
    public DAuthRole role;

    public DAuthRole getRole() {
        return this.role;
    }

    public DAuthIdentity getIdentity() {
        return identity;
    }
}
