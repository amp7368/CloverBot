package apple.discord.clover.database.activity.run;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.character.DCharacter;
import io.ebean.annotation.Index;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.UniqueConstraint;

@MappedSuperclass
@UniqueConstraint(columnNames = {"character_sku", "name"})
@Index(columnNames = {"character_sku", "name"})
public abstract class DSessionRunBase extends BaseEntity {

    @Id
    public UUID runId;

    @ManyToOne
    private DCharacter character;

    @Column
    private String name;

    public DSessionRunBase(String name, DCharacter character) {
        this.name = name;
        this.character = character;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public String getName() {
        return name;
    }
}
