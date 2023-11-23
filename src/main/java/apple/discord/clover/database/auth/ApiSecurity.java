package apple.discord.clover.database.auth;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.database.CloverDatabaseConfig;
import apple.discord.clover.database.auth.authentication.token.DAuthToken;
import apple.discord.clover.database.auth.identity.DAuthIdentity;
import apple.discord.clover.database.auth.permission.DefaultAuthPermission;
import com.password4j.Password;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.Handler;
import io.javalin.security.AccessManager;
import io.javalin.security.RouteRole;
import java.util.Set;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ApiSecurity implements AccessManager {

    public static String hashPassword(String password) {
        return Password.hash(password)
            .addRandomSalt()
            .addPepper(CloverDatabaseConfig.get().pepper)
            .withArgon2()
            .getResult();
    }

    @NotNull
    public static DAuthIdentity verifyRequestIdentity(Context ctx) {
        String tokenHeader = ctx.header("Authorization");
        if (tokenHeader == null) throw new ForbiddenResponse("'Authorization' header not provided");
        if (tokenHeader.length() <= "Bearer ".length()) throw new ForbiddenResponse("'Authorization' header is not in Bearer format");
        tokenHeader = tokenHeader.substring("Bearer ".length());

        DAuthToken token = AuthQuery.findToken(tokenHeader);
        if (token == null) throw new ForbiddenResponse("Invalid Authorization token");
        if (token.isExpired()) throw new ForbiddenResponse("Authorization token is expired");

        DAuthIdentity identity = token.getIdentity();
        if (identity == null) throw new ForbiddenResponse("Token has no identity?");

        return identity;
    }

    public static void verifyPlayerDataPermission(Context ctx, PlayerNameOrUUID player) {
        // todo
        if (true) return;

        DAuthIdentity identity = verifyRequestIdentity(ctx);
        boolean permissionAll = identity.hasPermissions(DefaultAuthPermission.ALL_PLAYER_DATA.get());
        if (permissionAll) return;

        boolean permissionMyself = identity.hasPermissions(DefaultAuthPermission.MY_PLAYER_DATA.get());
        if (permissionMyself) {
            UUID myself = AuthQuery.getMinecraft(identity);
            if (player.uuid().equals(myself)) {
                return;
            }
        }
        throw permissionException(player.username());
    }

    private static ForbiddenResponse permissionException(String player) {
        return new ForbiddenResponse("Cannot view \"%s\"'s player data".formatted(player));
    }

    public static boolean checkPassword(String hashed, String input) {
        return Password.check(input, hashed).addPepper(CloverDatabaseConfig.get().pepper).withArgon2();
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context ctx, @NotNull Set<? extends RouteRole> routeRoles) throws Exception {
        if (routeRoles.isEmpty()) {
            handler.handle(ctx);
            return;
        }
        // todo
        if (true) {
            handler.handle(ctx);
            return;
        }
        DAuthIdentity identity = verifyRequestIdentity(ctx);

        if (identity.hasPermissions(routeRoles)) handler.handle(ctx);
        else throw new ForbiddenResponse("Insufficient permissions");
    }

}
