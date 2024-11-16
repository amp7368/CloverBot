package apple.discord.clover.database.web.user;

import apple.discord.clover.api.base.BaseEntity;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "duser")
public class DUser extends BaseEntity {

    @Id
    private UUID id;
    @OneToOne(cascade = CascadeType.ALL)
    @Column
    private DUserMinecraft minecraft;
    @OneToOne(cascade = CascadeType.ALL)
    @Column
    private DUserDiscord discordId;

    public DUser(DUserMinecraft minecraft, DUserDiscord discordId) {
        this.minecraft = minecraft;
        this.discordId = discordId;
    }

    @NotNull
    public DUserMinecraft getMinecraft() {
        return this.minecraft;
    }
}
