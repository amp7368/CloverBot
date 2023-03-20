package apple.discord.clover.database.activity.partial;

import apple.discord.clover.database.activity.blacklist.BlacklistStorage;
import apple.discord.clover.database.activity.partial.query.QDLoginQueue;
import io.ebean.typequery.PString;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class LoginStorage {

    public static final int FIND_UPDATES_ROW_LIMIT = 100;

    public static void queuePlayers(List<String> players, Instant requestedAt) {
        QDLoginQueue alias = QDLoginQueue.alias();
        new QDLoginQueue().asUpdate().set(alias.isOnline, false).update();
        for (String player : players) {
            int didUpdate = new QDLoginQueue().where().player.eq(player).asUpdate().set(alias.isOnline, true).update();
            if (didUpdate == 0)
                new DLoginQueue(player, requestedAt).save();
        }
        new QDLoginQueue().where().isOnline.eq(false).asUpdate().setRaw("offline = offline + 1").setRaw("leave_time = now()").update();
    }

    public static List<DLoginQueue> findUpdates() {
        PString<QDLoginQueue> aPlayer = QDLoginQueue.alias().player;
        Timestamp lastAllowedFailure = Timestamp.from(BlacklistStorage.getLastAllowedFailure());
        return new QDLoginQueue().where().and().blacklist.isNotNull().blacklist.lastFailure.before(lastAllowedFailure).endAnd()
            .select(aPlayer)
            .orderBy().offline.desc().setMaxRows(FIND_UPDATES_ROW_LIMIT)
            .findList();
    }

    public static void success(DLoginQueue login) {
        login.delete();
        BlacklistStorage.success(login);
    }

    public static void failure(DLoginQueue login) {
        BlacklistStorage.failure(login);
    }
}
