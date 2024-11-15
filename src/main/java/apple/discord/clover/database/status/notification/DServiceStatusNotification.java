package apple.discord.clover.database.status.notification;

import apple.discord.clover.database.status.DServiceStatus;
import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "service_status_notification")
public class DServiceStatusNotification extends Model {

    @Id
    protected UUID id;
    @Column(nullable = false)
    protected Timestamp createdAt;
    @ManyToOne
    protected DServiceStatus status;
    @Column(nullable = false)
    protected NotificationType type;
    @Column(nullable = false)
    protected NotificationSuccess success;
    @Column
    protected long messageId;
    @Column
    protected long channelId;
    @Column(length = -1)
    protected String errorMessage;

    public DServiceStatusNotification(Instant createdAt, DServiceStatus status, NotificationType type) {
        this.createdAt = Timestamp.from(createdAt);
        this.status = status;
        this.type = type;
        this.success = NotificationSuccess.PENDING;
    }

    @NotNull
    public NotificationType getType() {
        return type;
    }

    @NotNull
    public Instant getCreatedAt() {
        return createdAt.toInstant();
    }

    public Duration timeSince() {
        return Duration.between(getCreatedAt(), Instant.now());
    }

    public NotificationSuccess getSuccess() {
        return this.success;
    }

    public DServiceStatusNotification setSuccess(NotificationSuccess success) {
        this.success = success;
        return this;
    }

    public void markSuccess(Message message) {
        this.success = NotificationSuccess.SENT;
        this.messageId = message.getIdLong();
        this.channelId = message.getChannelIdLong();
        save();
    }

    public void markFailure(Throwable throwable) {
        this.success = NotificationSuccess.FAILED;
        this.errorMessage = throwable.getMessage();
        save();
    }
}
