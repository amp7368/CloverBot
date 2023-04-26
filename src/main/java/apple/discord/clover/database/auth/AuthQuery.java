package apple.discord.clover.database.auth;

import apple.discord.clover.api.auth.signup.request.SignupRequest;
import apple.discord.clover.database.auth.authentication.token.AuthTokenGenerator;
import apple.discord.clover.database.auth.authentication.token.DAuthToken;
import apple.discord.clover.database.auth.authentication.token.query.QDAuthToken;
import apple.discord.clover.database.auth.identity.DAuthIdentity;
import apple.discord.clover.database.auth.identity.DUserBasicCredentials;
import apple.discord.clover.database.auth.identity.query.QDUserBasicCredentials;
import apple.discord.clover.database.auth.permission.DAuthPermission;
import apple.discord.clover.database.user.DUser;
import apple.discord.clover.database.user.DUserDiscord;
import apple.discord.clover.database.user.DUserMinecraft;
import apple.discord.clover.database.util.DBUnique;
import io.ebean.DB;
import io.ebean.Transaction;
import io.javalin.http.UnauthorizedResponse;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AuthQuery {

    @Nullable
    public static DAuthToken findToken(@NotNull String token) {
        return new QDAuthToken().where().token.eq(DAuthToken.decodeUrlToken(token)).findOne();
    }


    @NotNull
    public static DAuthPermission computePermission(UUID id, String name) {
        DAuthPermission permission = DB.find(DAuthPermission.class, id);
        if (permission != null) return permission;
        permission = new DAuthPermission(id, name);
        permission.save();
        return permission;
    }

    public static DUser getUser(DAuthIdentity identity) {
        DUserBasicCredentials basicCredentials = new QDUserBasicCredentials().where().authIdentity.eq(identity).findOne();
        if (basicCredentials != null) return basicCredentials.getUser();
        return null;
    }

    public static UUID getMinecraft(DAuthIdentity identity) {
        DUser user = getUser(identity);
        if (user == null) return null;
        return user.getMinecraft().getUUID();
    }

    @NotNull
    public static DAuthToken login(String username, String password) {
        DUserBasicCredentials userCredentials = new QDUserBasicCredentials()
            .where().and()
            .username.eq(username)
            .endAnd()
            .findOne();
        boolean badLogin = userCredentials == null || !userCredentials.isValidPassword(password);
        if (badLogin) throw new UnauthorizedResponse("Invalid username and/or password");
        DAuthIdentity authIdentity = userCredentials.getAuthIdentity();
        return AuthTokenGenerator.create(authIdentity);
    }

    @NotNull
    public static DAuthToken signup(SignupRequest request) {
        DUserMinecraft minecraft = new DUserMinecraft(request.getPlayer().uuid());
        @Nullable DUserDiscord discord = request.discordId == null ? null : new DUserDiscord(request.discordId);
        DUser user = new DUser(minecraft, discord);

        DAuthIdentity identity = new DAuthIdentity();
        DUserBasicCredentials credentials = new DUserBasicCredentials(user, request.username, request.password, identity);

        try (Transaction transaction = DB.beginTransaction()) {
            DBUnique.checkUniqueness(transaction, minecraft, discord, user, identity, credentials);

            if (discord != null) discord.save(transaction);
            minecraft.save(transaction);
            user.save(transaction);
            identity.save(transaction);
            credentials.save(transaction);

            transaction.commit();
        }
        return AuthTokenGenerator.create(identity);
    }
}
