package apple.discord.clover.api.auth.signup.request;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.constraint.ObjectConstraint;
import am.ik.yavi.constraint.password.PasswordPolicy;
import apple.discord.clover.api.base.request.HasPlayerRequest;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.api.base.validate.AppValidator;
import apple.discord.clover.discord.DiscordModule;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;

public class SignupRequest implements HasPlayerRequest {

    private static final Predicate<String> USERNAME_REGEX = Pattern.compile("\\w+").asMatchPredicate();
    public static final AppValidator<SignupRequest> VALIDATOR = new AppValidator<>(
        List.of(SignupRequest::passwordValidation, HasPlayerRequest::hasPlayerValidator, SignupRequest::discordIdValidation,
            SignupRequest::usernameValidation));
    private final transient PlayerNameOrUUID minecraftNameOrUUID = new PlayerNameOrUUID();

    public Long discordId;
    public String minecraft;
    public String username;
    public String password;


    private static ValidatorBuilder<SignupRequest> discordIdValidation(ValidatorBuilder<SignupRequest> validator) {
        return validator.constraint((SignupRequest req) -> req.discordId, "discordId",
            c -> c.predicateNullable(SignupRequest::isValidDiscordUser, "discord.userId",
                "{}, if provided, must be a must be a valid discord user id in a server with CloverBot"));
    }

    private static boolean isValidDiscordUser(Long inputUser) {
        if (inputUser == null) {
            return true;
        } else {
            @Nullable User foundUser = DiscordModule.dcf.jda().retrieveUserById(inputUser).complete();
            return foundUser != null && !foundUser.isBot() && !foundUser.isSystem();
        }
    }

    private static ValidatorBuilder<SignupRequest> usernameValidation(ValidatorBuilder<SignupRequest> validator) {
        return validator._charSequence(req -> req.username, "username",
            c -> c.notNull().cast()
                .predicate(USERNAME_REGEX, "user.username", "Username must follow [a-zA-Z0-9_]+. {} was provided."));
    }

    private static ValidatorBuilder<SignupRequest> passwordValidation(ValidatorBuilder<SignupRequest> validator) {
        return validator.constraintOnObject(req -> req.password, "password", SignupRequest::passwordPolicy);
    }

    private static ObjectConstraint<SignupRequest, String> passwordPolicy(ObjectConstraint<SignupRequest, String> constraint) {
        return constraint.notNull().password(
            p -> p.required(PasswordPolicy.LOWERCASE, PasswordPolicy.UPPERCASE, PasswordPolicy.NUMBERS).build());
    }

    @Override
    public PlayerNameOrUUID getPlayer() {
        return minecraftNameOrUUID;
    }

    @Override
    public String getPlayerString() {
        return this.minecraft;
    }
}

