package apple.discord.clover.wynncraft;

import com.google.gson.Gson;
import java.io.Reader;

public class WynncraftErrorResponse {

    public String Error;

    public static WynncraftErrorResponse readError(Reader reader) {
        return new Gson().fromJson(reader, WynncraftErrorResponse.class);
    }

    public String getErrorString() {
        return Error;
    }

    public WynncraftErrorType toError() {
        if (Error.equals("No player found with that username.")) {
            return WynncraftErrorType.PLAYER_NOT_FOUND;
        } else {
            return WynncraftErrorType.UNKNOWN;
        }
    }
}
