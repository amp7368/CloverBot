package apple.discord.clover.database.auth.permission;

import apple.discord.clover.api.base.BaseEntity;
import io.javalin.security.RouteRole;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "auth_permission")
public class DAuthPermission extends BaseEntity implements RouteRole {

    @Id
    private UUID id;
    @Column(unique = true)
    private String name;


    public DAuthPermission(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
