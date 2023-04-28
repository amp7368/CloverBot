package apple.discord.clover.api.auth;

import apple.discord.clover.api.auth.grant.request.GrantRequest;
import apple.discord.clover.api.auth.role.CreateRoleRequest;
import apple.discord.clover.api.auth.signup.request.SignupRequest;
import apple.discord.clover.api.base.ApiController;
import apple.discord.clover.database.auth.AuthQuery;
import apple.discord.clover.database.auth.authentication.token.DAuthToken;
import io.javalin.Javalin;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.security.BasicAuthCredentials;
import org.jetbrains.annotations.NotNull;

public class AuthController extends ApiController {

    public AuthController() {
        super("/auth");
    }

    @Override
    public void register(Javalin app) {
        app.post(this.path("/login"), this::login);
        app.post(this.path("/signup"), this::signup);
        app.post(this.path("/grant"), this::grant);
        app.post(this.path("/role/create"), this::roleCreate);//, DefaultAuthPermission.GRANT_PERMISSION.get());
    }

    private void roleCreate(Context ctx) {
        CreateRoleRequest request = this.checkBodyAndGet(ctx, CreateRoleRequest.class);
        this.checkErrors(ctx, CreateRoleRequest.VALIDATOR.validator().validate(request));
        AuthQuery.createRole(request);
        ctx.status(HttpStatus.OK);
    }

    private void grant(Context ctx) {
        GrantRequest request = this.checkBodyAndGet(ctx, GrantRequest.class);
        this.checkErrors(ctx, GrantRequest.VALIDATOR.validator().validate(request));
        AuthQuery.grant(request);
        ctx.status(HttpStatus.OK);
    }

    private void signup(Context ctx) {
        SignupRequest request = this.checkBodyAndGet(ctx, SignupRequest.class);
        this.checkErrors(ctx, SignupRequest.VALIDATOR.validator().validate(request));
        request.fetchPlayer().checkNotNull();
        @NotNull DAuthToken login = AuthQuery.signup(request);
        ctx.json(new AuthResponse(login.getUrlToken()));
    }

    private void login(Context ctx) {
        BasicAuthCredentials credentials = ctx.basicAuthCredentials();
        if (credentials == null) throw new BadRequestResponse("Login not found in 'Authorization' header");
        @NotNull DAuthToken login = AuthQuery.login(credentials.getUsername(), credentials.getPassword());
        ctx.json(new AuthResponse(login.getUrlToken()));
    }
}
