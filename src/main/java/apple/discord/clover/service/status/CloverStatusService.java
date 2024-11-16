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
import apple.discord.clover.service.status.service.CloverDiscordBotService;
import apple.discord.clover.service.status.service.CloverPlaySessionService;
import apple.discord.clover.service.status.service.CloverProgramService;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class CloverStatusService {

    private ServiceActivityDownConfig config;

    public static void load() {
        new CloverProgramService().start();
        new CloverPlaySessionService().start();
        new CloverDiscordBotService().start();
    }


    protected void start() {
        init();

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
        if (lastReport == null) NotificationApi.notify(NotificationType.REPORT, status, isSilent);
    }

    protected void markOffline(Instant currentUpdate, boolean isSilent) {
        DServiceStatus status = ServiceStatusApi.mark(getActivity(), currentUpdate, false);
        Duration timeDown = status.getDuration();

        DServiceStatusNotification lastReport = status.getLastNotification(NotificationType.REPORT);
        boolean shouldReport = getStatusConfig().shouldNotify(timeDown, lastReport);
        if (shouldReport) NotificationApi.notify(NotificationType.REPORT, status, isSilent);

        DServiceStatusNotification lastPing = status.getLastNotification(NotificationType.PING);
        boolean shouldPing = getStatusConfig().shouldPing(timeDown, lastPing);
        if (shouldPing) NotificationApi.notify(NotificationType.PING, status, isSilent);
    }

    protected ServiceActivityDownConfig getStatusConfig() {
        if (config != null) return config;
        return config = CloverConfig.getService().getStatus(getActivity());
    }

    protected abstract ServiceActivityType getActivity();

    protected Duration getPeriod() {
        return Duration.ofMinutes(1);
    }
}
