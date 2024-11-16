package apple.discord.clover.database.meta.status;

import apple.discord.clover.database.meta.status.query.QDServiceStatus;
import java.time.Instant;
import org.jetbrains.annotations.Nullable;

public interface ServiceStatusApi {

    static void extendOnline(ServiceActivityType activity, Instant lastOnline) {
        DServiceStatus lastStatus = findMostRecent(activity);
        if (lastStatus == null) return;
        if (!lastStatus.isOnline()) return;
        lastStatus.extendStatus(lastOnline).save();
    }

    static DServiceStatus mark(ServiceActivityType activity, Instant currentUpdate, boolean online) {
        DServiceStatus lastStatus = findMostRecent(activity);
        DServiceStatus update;
        if (lastStatus == null)
            update = new DServiceStatus(null, activity, online, currentUpdate, currentUpdate);
        else if (lastStatus.isOnline() != online)
            update = new DServiceStatus(lastStatus, activity, online, lastStatus.getEnd(), currentUpdate);
        else update = lastStatus;

        update.extendStatus(currentUpdate).save();
        return update;
    }

    @Nullable
    static DServiceStatus findMostRecent(ServiceActivityType activity) {
        return new QDServiceStatus().where()
            .activity.eq(activity)
            .orderBy().startAt.desc()
            .setMaxRows(1)
            .findOne();
    }
}
