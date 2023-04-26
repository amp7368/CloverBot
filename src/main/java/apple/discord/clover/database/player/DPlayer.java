package apple.discord.clover.database.player;

import apple.discord.clover.api.base.BaseEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class DPlayer extends BaseEntity {

    @Id
    @Column
    public UUID uuid;

    @Column
    public String username;

    public DPlayer(UUID player, String username) {
        this.uuid = player;
        this.username = username;
    }

    public DPlayer setUsername(String username) {
        this.username = username;
        return this;
    }
}
