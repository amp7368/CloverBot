package apple.discord.clover.database.auth;

import apple.discord.clover.api.auth.grant.request.GrantRequest;
import apple.discord.clover.api.auth.role.CreateRoleRequest;
import apple.discord.clover.api.auth.signup.request.SignupRequest;
import apple.discord.clover.database.auth.authentication.token.AuthTokenGenerator;
import apple.discord.clover.database.auth.authentication.token.DAuthToken;
import apple.discord.clover.database.auth.authentication.token.query.QDAuthToken;
import apple.discord.clover.database.auth.identity.DAuthIdentity;
import apple.discord.clover.database.auth.identity.DIdentityRoleBridge;
import apple.discord.clover.database.auth.identity.DUserBasicCredentials;
import apple.discord.clover.database.auth.identity.query.QDUserBasicCredentials;
import apple.discord.clover.database.auth.permission.DAuthPermission;
import apple.discord.clover.database.auth.permission.query.QDAuthPermission;
import apple.discord.clover.database.auth.role.DAuthRole;
import apple.discord.clover.database.auth.role.DRolePermissionBridge;
import apple.discord.clover.database.auth.role.query.QDAuthRole;
import apple.discord.clover.database.user.DUser;
import apple.discord.clover.database.user.DUserDiscord;
import apple.discord.clover.database.user.DUserMinecraft;
import apple.discord.clover.database.util.DBUnique;
import io.ebean.DB;
import io.ebean.DuplicateKeyException;
import io.ebean.Transaction;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.ConflictResponse;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.UnauthorizedResponse;
import java.util.List;
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
            transaction.setPersistCascade(true);
            DBUnique.checkUniqueness(transaction, minecraft, discord, user, identity, credentials);

            user.save(transaction);
            identity.save(transaction);
            credentials.save(transaction);
            transaction.commit();
        }
        return AuthTokenGenerator.create(identity);
    }

    public static void grant(GrantRequest request) {
        DUserBasicCredentials user = new QDUserBasicCredentials().username.eq(request.username).findOne();
        if (user == null) throw new NotFoundResponse("User '%s' was not found".formatted(request.username));
        List<DAuthRole> roles = new QDAuthRole().where().name.in(request.roles).findList();
        if (roles.size() != request.roles.length) {
            String requestedMessage = String.join(",", request.roles);
            String foundMessage = String.join(",", roles.stream().map(DAuthRole::getName).toArray(String[]::new));
            throw new BadRequestResponse("Not all roles [%s] exist. Found [%s]".formatted(requestedMessage, foundMessage));
        }
        DAuthIdentity authIdentity = user.getAuthIdentity();
        try (Transaction transaction = DB.beginTransaction()) {
            for (DAuthRole role : roles) {
                DIdentityRoleBridge roleBridge = new DIdentityRoleBridge(authIdentity, role);
                authIdentity.grantRole(roleBridge);
            }
            authIdentity.save(transaction);
            transaction.commit();
        } catch (DuplicateKeyException e) {
            throw new ConflictResponse("User '%s' already has at least one of the roles being granted.".formatted(request.username));
        }
    }

    public static void createRole(CreateRoleRequest request) {
        List<DAuthPermission> permissions = new QDAuthPermission().where().name.in(request.permissions).findList();
        if (permissions.size() != request.permissions.length) {
            String requestedMessage = String.join(",", request.permissions);
            String foundMessage = String.join(",", permissions.stream().map(DAuthPermission::getName).toArray(String[]::new));
            throw new BadRequestResponse("Not all roles [%s] exist. Found [%s]".formatted(requestedMessage, foundMessage));
        }
        DAuthRole role = new DAuthRole(request.name);
        try (Transaction transaction = DB.beginTransaction()) {
            transaction.setPersistCascade(true);
            for (DAuthPermission permission : permissions) {
                DRolePermissionBridge permissionBridge = new DRolePermissionBridge(role, permission);
                role.grantPermission(permissionBridge);
            }
            role.save(transaction);
            transaction.commit();
        } catch (DuplicateKeyException e) {
            throw new ConflictResponse("Role '%s' already exists.".formatted(request.name));
        }
    }
}
