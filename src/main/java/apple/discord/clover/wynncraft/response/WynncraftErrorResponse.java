package apple.discord.clover.wynncraft.response;

import com.google.gson.Gson;

public class WynncraftErrorResponse {

    public String Error;

    public static WynncraftErrorResponse readError(String reader) {
        return new Gson().fromJson(reader, WynncraftErrorResponse.class);
    }

    public String getErrorString() {
        return Error;
    }

    public WynncraftErrorType toError() {
        if (this.Error != null && Error.equalsIgnoreCase("No player found with that UUID.")) {
            return WynncraftErrorType.PLAYER_NOT_FOUND;
        } else if (this.Error != null && Error.equalsIgnoreCase("No player found with that username.")) {
            return WynncraftErrorType.PLAYER_NOT_FOUND;
        } else {
            return WynncraftErrorType.UNKNOWN;
        }
    }
}
