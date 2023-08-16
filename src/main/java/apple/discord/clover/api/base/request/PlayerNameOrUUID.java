package apple.discord.clover.api.base.request;

import io.javalin.http.NotFoundResponse;
import java.util.UUID;

public class PlayerNameOrUUID {

    private String username;
    private UUID uuid;

    public PlayerNameOrUUID set(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
        return this;
    }

    public UUID uuid() {
        return this.uuid;
    }

    public String username() {
        return username;
    }

    public void checkNotNull() throws NotFoundResponse {
        boolean uuidIsNull = this.uuid == null;
        boolean isNull = this.username == null || uuidIsNull;
        if (isNull) {
            String error = uuidIsNull ?
                "Player with username '%s' was not found".formatted(username) :
                "Player with UUID '%s' was not found".formatted(uuid);
            throw new NotFoundResponse(error);
        }
    }
}
