package apple.discord.clover.database.meta.status.notification;

import static apple.discord.clover.discord.system.theme.CloverMessages.formatDateLong;

import apple.discord.clover.CloverConfig;
import apple.discord.clover.database.meta.status.DServiceStatus;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public interface NotificationApi {

    static void notify(NotificationType notificationType, DServiceStatus status, boolean isSilent) {
        DServiceStatusNotification notification = new DServiceStatusNotification(Instant.now(), status, notificationType);
        status.addNotification(notification);
        notification.save();

        MessageCreateBuilder msg = makeMessage(notificationType, status, isSilent);

        CloverConfig.getDiscord().getStatusChannel()
            .sendMessage(msg.build())
            .queue(notification::markSuccess, notification::markFailure);
    }

    private static MessageCreateBuilder makeMessage(NotificationType notificationType, DServiceStatus status,
        boolean isSilent) {
        EmbedBuilder embed = new EmbedBuilder();
        status.embed(embed);

        DServiceStatus previousStatus = status.getPrevious();
        if (previousStatus != null) previousStatus.durationEmbed(embed);

        if (notificationType == NotificationType.PING) {
            String pingMention = CloverConfig.getService().getPingMention();
            embed.appendDescription("\n%s\n".formatted(pingMention));
        }

        DServiceStatusNotification lastNotification = status.getLastNotification(notificationType);
        if (lastNotification != null) {
            String success = lastNotification.getSuccess().pastTense();
            String type = notificationType.display();
            String date = formatDateLong(lastNotification.getCreatedAt());
            embed.setFooter("Most recent %s for this %s on %s\n".formatted(type, success, date));
        }
        embed.setTimestamp(Instant.now());

        MessageCreateBuilder msg = new MessageCreateBuilder()
            .setEmbeds(embed.build());

        if (isSilent) msg = msg.mentionUsers(CloverConfig.getService().getPingTarget());
        else msg = msg.setAllowedMentions(List.of());

        return msg;
    }

    static void resend(DServiceStatus status, DServiceStatusNotification lastNotification) {
        makeMessage(lastNotification.getType(), status, false);
        lastNotification.setSuccess(NotificationSuccess.RESENT);
        lastNotification.save();
    }
}
