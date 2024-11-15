package apple.discord.clover.database.status.notification;

import apple.utilities.util.Pretty;
import io.ebean.annotation.DbEnumValue;
import org.jetbrains.annotations.NotNull;


public enum NotificationSuccess {
    PENDING,
    FAILED,
    RESENT,
    SENT;

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
            case RESENT -> "was resent";
            case SENT -> "sent successfully";
        };
    }
}
