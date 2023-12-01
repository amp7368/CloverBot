package apple.discord.clover.wynncraft;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import okhttp3.Response;

public class WynnHeaders {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

    public static Instant date(Response response) {
        String header = response.header("date");
        Instant defaultIfNull = Instant.now();
        if (header == null) return defaultIfNull;
        return Instant.from(FORMATTER.parse(header));
    }

    public static Instant expires(Response response) {
        String header = response.header("expires");
        Instant defaultIfNull = Instant.now().plus(Duration.ofMinutes(5));
        if (header == null) return defaultIfNull;
        return Instant.from(FORMATTER.parse(header));
    }
}
