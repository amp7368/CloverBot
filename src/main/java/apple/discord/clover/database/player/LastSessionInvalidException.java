package apple.discord.clover.database.player;

import java.sql.SQLException;

public class LastSessionInvalidException extends SQLException {

    public LastSessionInvalidException(String reason, String SQLState, int vendorCode) {
        super(reason, SQLState, vendorCode);
    }

    public LastSessionInvalidException(String reason, String SQLState) {
        super(reason, SQLState);
    }

    public LastSessionInvalidException(String reason) {
        super(reason);
    }

    public LastSessionInvalidException() {
    }

    public LastSessionInvalidException(Throwable cause) {
        super(cause);
    }

    public LastSessionInvalidException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public LastSessionInvalidException(String reason, String sqlState, Throwable cause) {
        super(reason, sqlState, cause);
    }

    public LastSessionInvalidException(String reason, String sqlState, int vendorCode, Throwable cause) {
        super(reason, sqlState, vendorCode, cause);
    }
}
