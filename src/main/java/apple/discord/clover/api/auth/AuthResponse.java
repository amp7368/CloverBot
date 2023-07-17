package apple.discord.clover.api.auth;

import java.time.Instant;

public class AuthResponse {

    public Instant expiration;
    public String token;

    public AuthResponse(String token, Instant expiration) {
        this.token = token;
        this.expiration = expiration;
    }
}
