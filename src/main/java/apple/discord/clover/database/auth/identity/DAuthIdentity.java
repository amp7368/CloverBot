package apple.discord.clover.database.auth.identity;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.auth.authentication.DAuthentication;
import apple.discord.clover.database.auth.permission.DAuthPermission;
import io.javalin.security.RouteRole;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "auth_identity")
public class DAuthIdentity extends BaseEntity {

    @Id
    private UUID id;

    @Column
    @OneToMany(cascade = CascadeType.ALL)
    private List<DIdentityRoleBridge> roles;
    @Column
    @OneToMany(cascade = CascadeType.ALL)
    private List<DAuthentication> authentications;
    private transient Set<DAuthPermission> permissionSet;

    private Set<DAuthPermission> getPermissionSet() {
        if (this.permissionSet != null) return this.permissionSet;
        permissionSet = new HashSet<>();
        for (DIdentityRoleBridge role : roles) {
            permissionSet.addAll(role.getRole().getPermissions());
        }
        return permissionSet = Collections.unmodifiableSet(permissionSet);
    }

    public boolean hasPermissions(RouteRole... routeRole) {
        return hasPermissions(List.of(routeRole));
    }

    public boolean hasPermissions(Collection<? extends RouteRole> routeRole) {
        @SuppressWarnings("all") boolean hasAll = this.getPermissionSet().containsAll(routeRole);
        return hasAll;
    }

    public void grantRole(DIdentityRoleBridge role) {
        roles.add(role);
    }
}
