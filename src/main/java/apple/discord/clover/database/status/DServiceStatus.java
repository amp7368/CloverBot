package apple.discord.clover.database.status;

import static apple.discord.clover.discord.system.theme.CloverMessages.formatDateLong;

import apple.discord.clover.database.status.notification.DServiceStatusNotification;
import apple.discord.clover.database.status.notification.NotificationType;
import apple.discord.clover.discord.DiscordBot;
import apple.discord.clover.discord.system.theme.CloverColor;
import apple.discord.clover.discord.system.theme.CloverEmoji;
import io.ebean.Model;
import io.ebean.annotation.Index;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import net.dv8tion.jda.api.EmbedBuilder;

@Entity
@Table(name = "service_status")
public class DServiceStatus extends Model {

    @Id
    protected UUID id;
    @Index
    @Column(nullable = false)
    protected ServiceActivityType activity;
    @Column(nullable = false)
    protected boolean isOnline;
    @Index
    @Column(nullable = false)
    protected Timestamp startAt;
    @Column(nullable = false)
    protected Timestamp endAt;
    @OneToMany
    protected List<DServiceStatusNotification> notifications = new ArrayList<>();
    @JoinColumn
    @OneToOne
    protected DServiceStatus previous;

    public DServiceStatus(DServiceStatus previous, ServiceActivityType activity, boolean isOnline, Instant start,
        Instant lastNoticed) {
        this.previous = previous;
        this.activity = activity;
        this.isOnline = isOnline;
        this.startAt = Timestamp.from(start);
        this.endAt = Timestamp.from(lastNoticed);
    }

    public List<DServiceStatusNotification> getNotifications(NotificationType type) {
        Comparator<DServiceStatusNotification> sort = Comparator.comparing(DServiceStatusNotification::getCreatedAt).reversed();
        return notifications.stream()
            .filter(typ -> typ.getType() == type)
            .sorted(sort)
            .toList();
    }

    public DServiceStatusNotification getLastNotification(NotificationType type) {
        List<DServiceStatusNotification> notifications = getNotifications(type);
        if (notifications.isEmpty()) return null;
        return notifications.getFirst();
    }

    public boolean isOnline() {
        return this.isOnline;
    }

    public DServiceStatus extendStatus(Instant lastNoticed) {
        this.endAt = Timestamp.from(lastNoticed);
        return this;
    }

    public Instant getStart() {
        return startAt.toInstant();
    }

    public Instant getEnd() {
        return endAt.toInstant();
    }

    public Duration getDuration() {
        return Duration.between(getStart(), getEnd());
    }

    public DServiceStatus getPrevious() {
        return previous;
    }

    public void embed(EmbedBuilder embed) {
        String status = isOnline() ? "Online" : "Offline";
        CloverEmoji emoji = isOnline() ? CloverEmoji.STATUS_ACTIVE : CloverEmoji.STATUS_OFFLINE;
        String title = "## %s - %s\n".formatted(this.activity.display(), emoji.spaced(status));
        embed.appendDescription(title);
        embed.setAuthor("Status", null, DiscordBot.getSelfAvatar());

        int color = isOnline() ? CloverColor.GREEN : CloverColor.RED;
        embed.setColor(color);
    }

    public void durationEmbed(EmbedBuilder embed) {
        if (isOnline()) {
            embed.appendDescription("### %s have gone offline!\n".formatted(this.activity.display()));
            embed.appendDescription("**Active at:** %s\n".formatted(formatDateLong(getStart())));
            embed.appendDescription("**Offline at:** %s\n".formatted(formatDateLong(getEnd())));
        } else {
            embed.appendDescription("### %s have been restored!\n".formatted(this.activity.display()));
            embed.appendDescription("**Offline at:** %s\n".formatted(formatDateLong(getStart())));
            embed.appendDescription("**Restored at:** %s\n".formatted(formatDateLong(getEnd())));
        }
    }

    public void addNotification(DServiceStatusNotification notification) {
        this.notifications.add(notification);
    }
}
