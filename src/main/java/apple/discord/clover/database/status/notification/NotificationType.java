package apple.discord.clover.database.status.notification;

import io.ebean.annotation.DbEnumValue;

public enum NotificationType {
    REPORT,
    PING;

    @DbEnumValue
    public String id() {
        return name();
    }

    public String display() {
        return switch (this) {
            case REPORT -> "report";
            case PING -> "ping";
        };
    }

}
