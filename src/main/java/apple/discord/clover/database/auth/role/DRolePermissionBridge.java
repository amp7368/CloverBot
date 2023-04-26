package apple.discord.clover.database.auth.role;

import apple.discord.clover.database.auth.permission.DAuthPermission;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;

@Entity
@Table
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "role"), @PrimaryKeyJoinColumn(name = "permission")})
public class DRolePermissionBridge {

    @ManyToOne(optional = false)
    @Column
    public DAuthRole role;
    @Column
    @OneToOne(optional = false)
    public DAuthPermission permission;

    public DAuthPermission getPermission() {
        return permission;
    }

    public DAuthRole getRole() {
        return role;
    }
}
