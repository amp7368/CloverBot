package apple.discord.clover.database.user;

import apple.discord.clover.api.base.BaseEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_minecraft")
public class DUserMinecraft extends BaseEntity {

    @Id
    private UUID uuid;
    @OneToOne
    @Column
    private DUser user;

    public DUserMinecraft(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
}
