package apple.discord.clover.database.web.auth.authentication.token;

import apple.discord.clover.database.web.auth.authentication.DAuthentication;
import apple.discord.clover.database.web.auth.identity.DAuthIdentity;
import io.ebean.DB;
import io.ebean.Transaction;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import org.jetbrains.annotations.NotNull;

public class AuthTokenGenerator {

    private static final SecureRandom random = new SecureRandom();

    @NotNull
    public static DAuthToken create(DAuthIdentity identity) {
        Timestamp lastUsed = Timestamp.from(Instant.now());
        byte[] tokenBytes = new byte[DAuthToken.TOKEN_BYTES];
        random.nextBytes(tokenBytes);
        DAuthentication authentication = new DAuthentication(identity);
        DAuthToken token = new DAuthToken(authentication, tokenBytes, lastUsed);
        try (Transaction transaction = DB.beginTransaction()) {
            authentication.save(transaction);
            token.save(transaction);
            transaction.commit();
        }
        return token;
    }
}
