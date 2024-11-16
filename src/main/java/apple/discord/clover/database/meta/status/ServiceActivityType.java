package apple.discord.clover.database.meta.status;

import io.ebean.annotation.DbEnumValue;

public enum ServiceActivityType {
    PROGRAM,
    DISCORD_BOT,
    PLAY_SESSION;

    @DbEnumValue
    public String id() {
        return name();
    }

    public String display() {
        return switch (this) {
            case DISCORD_BOT -> "Discord Services";
            case PLAY_SESSION -> "Activity Services";
            case PROGRAM -> "All Services";
        };
    }
}
