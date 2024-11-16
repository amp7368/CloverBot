package apple.discord.clover.database.web.auth.identity;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.web.auth.ApiSecurity;
import apple.discord.clover.database.web.user.DUser;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_basic_credentials")
public class DUserBasicCredentials extends BaseEntity implements HasAuthIdentity {

    @Id
    private UUID id;
    @Column
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DUser user;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true)
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private DAuthIdentity authIdentity;

    public DUserBasicCredentials(DUser user, String username, String password, DAuthIdentity authIdentity) {
        this.user = user;
        this.username = username;
        this.password = ApiSecurity.hashPassword(password);
        this.authIdentity = authIdentity;
    }

    @Override
    public DAuthIdentity getAuthIdentity() {
        return this.authIdentity;
    }

    public DUser getUser() {
        return user;
    }

    public boolean isValidPassword(String password) {
        return ApiSecurity.checkPassword(this.password, password);
    }
}
