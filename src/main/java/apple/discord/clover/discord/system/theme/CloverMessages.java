package apple.discord.clover.discord.system.theme;

import apple.discord.clover.discord.DiscordModule;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class CloverMessages {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("LLL dd yyyy")
        .withZone(DiscordModule.TIME_ZONE);

    public static String formatDate(Instant date) {
        return formatDate(date, false);
    }

    public static String formatDateLong(Instant date) {
        return formatDateLong(date, false);
    }

    public static String formatDate(Instant date, boolean emoji) {
        String dateFormatted = DATE_FORMATTER.format(date);
        if (emoji) return CloverEmoji.ANY_DATE + " " + dateFormatted;
        return dateFormatted;
    }

    public static String formatDateLong(Instant date, boolean emoji) {
        long epoch = date.getEpochSecond();
        String dateFormatted = "<t:%d:D> <t:%d:T>".formatted(epoch, epoch);
        if (emoji) return CloverEmoji.ANY_DATE + " " + dateFormatted;
        return dateFormatted;
    }

}
