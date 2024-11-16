package apple.discord.clover.database.web.auth.role;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.web.auth.permission.DAuthPermission;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "auth_role")
public class DAuthRole extends BaseEntity {

    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @Column
    private List<DRolePermissionBridge> permissions;

    public DAuthRole(String name) {
        this.name = name;
    }

    public List<DAuthPermission> getPermissions() {
        return permissions.stream().map(DRolePermissionBridge::getPermission).toList();
    }

    public String getName() {
        return this.name;
    }

    public void grantPermission(DRolePermissionBridge permission) {
        this.permissions.add(permission);
    }
}
