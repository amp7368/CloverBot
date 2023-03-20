package apple.discord.clover.database.player;

import io.ebean.Model;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "player")
public class DPlayer extends Model {

    @Id
    @Column
    public UUID uuid;

    @Column
    public String username;

    public DPlayer(UUID player, String username) {
        this.uuid = player;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
