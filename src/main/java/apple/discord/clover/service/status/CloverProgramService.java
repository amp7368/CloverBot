package apple.discord.clover.service.status;

import apple.discord.clover.database.status.DServiceStatus;
import apple.discord.clover.database.status.ServiceActivityType;
import apple.discord.clover.database.status.ServiceStatusApi;
import java.time.Duration;
import java.time.Instant;

public class CloverProgramService extends CloverStatusService {

    @Override
    public void init() {
        DServiceStatus recent = ServiceStatusApi.findMostRecent(ServiceActivityType.PROGRAM);
        if (recent == null) return;
        Duration timeInactive = Duration.between(recent.getEnd(), Instant.now());
        boolean isDown = getStatusConfig().isConsideredDown(timeInactive);
        if (isDown) {
            markOffline(Instant.now(), true);
        }
    }


    @Override
    protected void run() {
        markOnline(Instant.now(), true);
    }

    @Override
    protected ServiceActivityType getActivity() {
        return ServiceActivityType.PROGRAM;
    }

    @Override
    protected Duration getPeriod() {
        return Duration.ofMinutes(1);
    }
}
