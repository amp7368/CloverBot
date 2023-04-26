package apple.discord.clover.database.auth.role;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.auth.permission.DAuthPermission;
import java.util.List;
import java.util.UUID;
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
    @OneToMany
    @Column
    private List<DRolePermissionBridge> permissions;

    public List<DAuthPermission> getPermissions() {
        return permissions.stream().map(DRolePermissionBridge::getPermission).toList();
    }
}
