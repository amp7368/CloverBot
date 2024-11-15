package apple.discord.clover.service.status;

import apple.discord.clover.CloverBot;
import apple.discord.clover.CloverConfig;
import apple.discord.clover.database.status.DServiceStatus;
import apple.discord.clover.database.status.ServiceActivityType;
import apple.discord.clover.database.status.ServiceStatusApi;
import apple.discord.clover.database.status.notification.DServiceStatusNotification;
import apple.discord.clover.database.status.notification.NotificationApi;
import apple.discord.clover.database.status.notification.NotificationType;
import apple.discord.clover.service.ServiceActivityDownConfig;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CloverStatusService {

    private ServiceActivityDownConfig config;

    public static void load() {
        CloverConfig.getService().load();
        new CloverProgramService().start();
        new CloverPlaySessionService().start();
//        todo new CloverDiscordBotService().start();
    }


    protected void start() {
        init();
        run();

        ScheduledExecutorService executor = CloverBot.get().executor();
        long period = getPeriod().getSeconds();
        long initialDelay = new SecureRandom().nextLong(5, period);
        executor.scheduleAtFixedRate(this::run, initialDelay, period, TimeUnit.SECONDS);
    }

    protected void init() {
    }

    protected abstract void run();

    protected void markOnline(Instant currentUpdate, boolean isSilent) {
        DServiceStatus status = ServiceStatusApi.mark(getActivity(), currentUpdate, true);

        DServiceStatusNotification lastReport = status.getLastNotification(NotificationType.REPORT);
        if (lastReport == null) NotificationApi.notify(NotificationType.REPORT, status, null, isSilent);
    }

    protected void markOffline(Instant currentUpdate, boolean isSilent) {
        DServiceStatus status = ServiceStatusApi.mark(getActivity(), currentUpdate, false);
        Duration timeDown = status.getDuration();

        DServiceStatusNotification lastReport = status.getLastNotification(NotificationType.REPORT);
        boolean shouldReport = getStatusConfig().shouldNotify(timeDown, lastReport);
        if (shouldReport) NotificationApi.notify(NotificationType.REPORT, status, lastReport, isSilent);

        DServiceStatusNotification lastPing = status.getLastNotification(NotificationType.PING);
        boolean shouldPing = getStatusConfig().shouldPing(timeDown, lastPing);
        if (shouldPing) NotificationApi.notify(NotificationType.PING, status, lastPing, isSilent);
    }

    protected ServiceActivityDownConfig getStatusConfig() {
        if (config != null) return config;
        return config = CloverConfig.getService().getStatus(getActivity());
    }

    protected abstract ServiceActivityType getActivity();

    protected abstract Duration getPeriod();

}
