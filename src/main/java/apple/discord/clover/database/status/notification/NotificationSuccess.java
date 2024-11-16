package apple.discord.clover.database.status.notification;

import apple.utilities.util.Pretty;
import io.ebean.annotation.DbEnumValue;
import org.jetbrains.annotations.NotNull;


public enum NotificationSuccess {
    PENDING,
    SENT,
    FAILED,
    RETRYING,
    RESENT;

    public boolean isHealthy() {
        return this != FAILED;
    }

    @DbEnumValue
    public String id() {
        return this.name();
    }

    @Override
    public String toString() {
        return display();
    }

    @NotNull
    public String display() {
        return Pretty.spaceEnumWords(name());
    }

    @NotNull
    public String pastTense() {
        return switch (this) {
            case PENDING -> "is pending";
            case FAILED -> "failed to send";
            case RETRYING -> "is resending";
            case RESENT -> "was resent";
            case SENT -> "sent successfully";
        };
    }
}
