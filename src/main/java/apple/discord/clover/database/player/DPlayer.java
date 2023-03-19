package apple.discord.clover.database.player;

import io.ebean.Model;
import io.ebean.annotation.DbJson;
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

    @DbJson
    public PlayerMoment current;

    public DPlayer(UUID player) {
        this.uuid = player;
    }
}
