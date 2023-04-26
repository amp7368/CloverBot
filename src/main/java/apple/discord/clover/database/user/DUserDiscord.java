package apple.discord.clover.database.user;

import apple.discord.clover.api.base.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_discord")
public class DUserDiscord extends BaseEntity {

    @Id
    private Long discordId;
    @OneToOne
    @Column
    private DUser user;

    public DUserDiscord(Long discordId) {
        this.discordId = discordId;
    }
}
