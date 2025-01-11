package apple.discord.clover.discord.command.activity.base.player;

import apple.utilities.util.Pretty;

public enum GuildRank {
    OWNER(5),
    CHIEF(4),
    STRATEGIST(3),
    CAPTAIN(2),
    RECRUITER(1),
    RECRUIT(0),
    UNKNOWN(0);

    private final int stars;

    GuildRank(int stars) {
        this.stars = stars;
    }

    public static GuildRank parse(String rank) {
        try {
            return valueOf(rank.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }

    public String getStars() {
        if (this == UNKNOWN) {
            return "???";
        }
        return "*".repeat(stars);
    }

    @Override
    public String toString() {
        return Pretty.spaceEnumWords(name());
    }
}
