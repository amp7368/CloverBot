package apple.discord.clover.database.character;

import apple.discord.clover.database.activity.DPlaySession;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "player_character")
public class DCharacter {

    @Id
    public UUID uuid;

    @ManyToOne
    public DPlaySession session;
    
    public String type;

}
