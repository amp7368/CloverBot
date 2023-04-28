package apple.discord.clover.database.auth.role;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.auth.permission.DAuthPermission;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.PrimaryKeyJoinColumns;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "auth_role_permission_bridge")
@PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name = "role"), @PrimaryKeyJoinColumn(name = "permission")})
@UniqueConstraint(columnNames = {"role", "permission"})
public class DRolePermissionBridge extends BaseEntity {

    @ManyToOne(optional = false)
    @Column
    public DAuthRole role;
    @Column
    @OneToOne(optional = false)
    public DAuthPermission permission;

    public DRolePermissionBridge(DAuthRole role, DAuthPermission permission) {
        this.role = role;
        this.permission = permission;
    }

    public DAuthPermission getPermission() {
        return permission;
    }

    public DAuthRole getRole() {
        return role;
    }
}
