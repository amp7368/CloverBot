package apple.discord.clover.api;

import io.ebean.DuplicateKeyException;
import io.javalin.Javalin;
import io.javalin.http.ConflictResponse;
import io.javalin.http.Context;

public class ExceptionHandlers {

    public static void registerExceptions(Javalin app) {
        app.exception(DuplicateKeyException.class, ExceptionHandlers::duplicateKeyException);
    }

    private static void duplicateKeyException(DuplicateKeyException e, Context ctx) {
        ApiModule.get().logger().error("Error encountered handling " + ctx.path(), e);
        throw new ConflictResponse("A resource created from this request already exists.");
    }

}
