package apple.discord.clover.database.meta.log;

import io.ebean.Model;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "command_log")
public class DCommandLog extends Model {

    @Id
    protected UUID id;
    @Column(nullable = false)
    protected Timestamp createdAt;
    @Column(nullable = false, length = -1)
    protected String commandMessage;

    @Column(nullable = false)
    protected Long senderDiscordId;
    @Column(nullable = false)
    protected String senderDiscordName;
    @Column(nullable = false)
    protected String senderEffectiveName;
    @Column(nullable = false)
    protected String senderAvatarUrl;

    @Column(nullable = false)
    protected long channelId;
    @Column(nullable = false)
    protected String channelName;
    @Column(nullable = false)
    protected String channelType;
    @Column
    protected Long serverId;
    @Column
    protected String serverName;

    public DCommandLog(String commandMessage, User senderDiscord,
        @Nullable Guild server, MessageChannelUnion channel) {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.commandMessage = commandMessage;

        this.senderDiscordId = senderDiscord.getIdLong();
        this.senderDiscordName = senderDiscord.getName();
        this.senderEffectiveName = senderDiscord.getEffectiveName();
        this.senderAvatarUrl = senderDiscord.getEffectiveAvatarUrl();

        this.channelId = channel.getIdLong();
        this.channelName = channel.getName();
        this.channelType = channel.getType().name();

        if (server != null) {
            this.serverId = server.getIdLong();
            this.serverName = server.getName();
        }
    }

    public String getCommandMessage() {
        return this.commandMessage;
    }

    public Instant getCreatedAt() {
        return createdAt.toInstant();
    }

    public MessageEmbed toMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setAuthor("@" + this.senderEffectiveName, null, this.senderAvatarUrl)
            .setTimestamp(this.getCreatedAt())
            .setDescription(this.commandMessage)
            .setFooter("#%s of %s".formatted(this.channelName, this.serverName));

        return embed.build();
    }
}
