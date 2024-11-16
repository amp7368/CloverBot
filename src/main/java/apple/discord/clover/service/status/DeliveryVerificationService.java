package apple.discord.clover.service.status;

import apple.discord.clover.database.meta.status.DServiceStatus;
import apple.discord.clover.database.meta.status.ServiceActivityType;
import apple.discord.clover.database.meta.status.notification.DServiceStatusNotification;
import apple.discord.clover.database.meta.status.notification.NotificationApi;
import apple.discord.clover.database.meta.status.notification.NotificationType;
import apple.discord.clover.database.meta.status.query.QDServiceStatus;

public class DeliveryVerificationService {

    private static void verify(ServiceActivityType activity) {
        DServiceStatus status = new QDServiceStatus().where()
            .activity.eq(activity)
            .orderBy().endAt.desc()
            .setMaxRows(1)
            .findOne();
        if (status == null) return;

        for (NotificationType type : NotificationType.values()) {
            verify(status, type);
        }
    }

    private static void verify(DServiceStatus status, NotificationType notificationType) {
        DServiceStatusNotification lastNotification = status.getLastNotification(notificationType);
        if (lastNotification == null) return;
        if (lastNotification.getSuccess().isHealthy()) return;
        NotificationApi.resend(status, lastNotification);
    }

    public static void queueFailed(DServiceStatus status, DServiceStatusNotification notification) {
    }

    private record DeliveryMessage(DServiceStatus status, DServiceStatusNotification notification) {

        void send() {
            NotificationApi.resend(status, notification);
        }
    }
}
