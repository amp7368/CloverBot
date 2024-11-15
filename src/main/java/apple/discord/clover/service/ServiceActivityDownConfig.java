package apple.discord.clover.service;

import apple.discord.clover.database.status.ServiceActivityType;
import apple.discord.clover.database.status.notification.DServiceStatusNotification;
import java.time.Duration;
import org.jetbrains.annotations.Nullable;

public class ServiceActivityDownConfig {

    protected String $comment = "All units in hours";
    protected double markDownAfter;

    protected double reportAfter;
    protected double pingAfter;
    protected double pingPeriod;

    public ServiceActivityDownConfig() {
    }

    public ServiceActivityDownConfig(double markDownAfter, double reportAfter, int pingAfter, int pingPeriod) {
        this.markDownAfter = markDownAfter;
        this.reportAfter = reportAfter;
        this.pingAfter = pingAfter;
        this.pingPeriod = pingPeriod;
    }

    public static ServiceActivityDownConfig createNoPing(ServiceActivityType ignored) {
        return new ServiceActivityDownConfig(0.25, 0.5, 1000000, 1000000);
    }

    public static ServiceActivityDownConfig createDefault(ServiceActivityType ignored) {
        return new ServiceActivityDownConfig(0.25, 0.5, 1, 2);
    }

    private static boolean isTime(Duration timeInactive, double minHours) {
        Duration minDuration = Duration.ofMinutes((long) (minHours * 60));
        return timeInactive.compareTo(minDuration) >= 0;
    }

    public boolean isConsideredDown(Duration timeInactive) {
        return isTime(timeInactive, markDownAfter);
    }

    public boolean shouldNotify(Duration timeDown, @Nullable DServiceStatusNotification lastReport) {
        if (!isTime(timeDown, reportAfter)) return false;
        return lastReport == null;
    }

    public boolean shouldPing(Duration timeDown, @Nullable DServiceStatusNotification lastPing) {
        if (!isTime(timeDown, reportAfter)) return false;

        if (lastPing == null) return true;
        Duration since = lastPing.timeSince();
        return isTime(since, pingPeriod);
    }

}
