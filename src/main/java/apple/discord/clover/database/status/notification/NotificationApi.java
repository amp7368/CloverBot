package apple.discord.clover.database.status.notification;

import static apple.discord.clover.discord.system.theme.CloverMessages.formatDateLong;

import apple.discord.clover.CloverConfig;
import apple.discord.clover.database.status.DServiceStatus;
import java.time.Instant;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.Nullable;

public interface NotificationApi {

    static void notify(NotificationType notificationType, DServiceStatus status,
        @Nullable DServiceStatusNotification lastNotification, boolean isSilent) {
        EmbedBuilder embed = new EmbedBuilder();
        status.embed(embed);

        DServiceStatus previousStatus = status.getPrevious();
        if (previousStatus != null) previousStatus.durationEmbed(embed);

        if (notificationType == NotificationType.PING) {
            String pingMention = CloverConfig.getService().getPingMention();
            embed.appendDescription("\n%s\n".formatted(pingMention));
        }

        if (lastNotification != null) {
            String success = lastNotification.getSuccess().pastTense();
            String type = notificationType.display();
            String date = formatDateLong(lastNotification.getCreatedAt());
            embed.setFooter("Most recent %s for this %s on %s\n".formatted(type, success, date));
        }
        Instant now = Instant.now();
        embed.setTimestamp(now);

        DServiceStatusNotification notification = new DServiceStatusNotification(now, status, notificationType);
        status.addNotification(notification);
        notification.save();

        MessageCreateBuilder msg = new MessageCreateBuilder()
            .setEmbeds(embed.build());

        if (isSilent) msg = msg.mentionUsers(CloverConfig.getService().getPingTarget());
        else msg = msg.setAllowedMentions(List.of());

        CloverConfig.getDiscord().getStatusChannel()
            .sendMessage(msg.build())
            .queue(notification::markSuccess, notification::markFailure);
    }
}
