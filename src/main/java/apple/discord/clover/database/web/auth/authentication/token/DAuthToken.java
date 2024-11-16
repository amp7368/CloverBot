package apple.discord.clover.database.web.auth.authentication.token;

import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.web.auth.authentication.DAuthentication;
import apple.discord.clover.database.web.auth.authentication.IsAuthentication;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "auth_token")
public class DAuthToken extends BaseEntity implements IsAuthentication {

    public static final int TOKEN_BYTES = 32;
    private static final Duration TIME_TO_EXPIRE = Duration.ofHours(1);
    @Id
    @Column(columnDefinition = "char(32)")
    private String token;
    @Column
    @OneToOne(optional = false)
    private DAuthentication authentication;
    @Column
    private Timestamp lastUsed;

    public DAuthToken(DAuthentication authentication, byte[] tokenBytes, Timestamp lastUsed) {
        this.authentication = authentication;
        this.token = new String(tokenBytes);
        this.lastUsed = lastUsed;
    }

    @NotNull
    public static String decodeUrlToken(@NotNull String token) {
        return new String(Base64.getUrlDecoder().decode(token.getBytes(StandardCharsets.ISO_8859_1)));
    }

    private static String encodeUrlToken(String token) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token.getBytes());
    }

    public boolean isExpired() {
        return getExpiration().isBefore(Instant.now());
    }

    @Override
    public DAuthentication getAuthentication() {
        return this.authentication;
    }

    public String getUrlToken() {
        return encodeUrlToken(this.token);
    }

    public Instant getExpiration() {
        return this.lastUsed.toInstant().plus(TIME_TO_EXPIRE);
    }
}
