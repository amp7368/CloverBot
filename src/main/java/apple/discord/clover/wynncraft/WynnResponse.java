package apple.discord.clover.wynncraft;

import io.avaje.lang.Nullable;
import java.time.Instant;
import okhttp3.Response;

public record WynnResponse<Data>(Instant retrieved, Instant expires, @Nullable Data data) {

    public WynnResponse(Response response, Data data) {
        this(WynnHeaders.date(response), WynnHeaders.expires(response), data);
    }

    public static <Res> WynnResponse<Res> createError(Response response) {
        return new WynnResponse<>(response, null);
    }

    public static <Res> WynnResponse<Res> createSuccess(Response response, Res data) {
        return new WynnResponse<>(response, data);
    }
}
