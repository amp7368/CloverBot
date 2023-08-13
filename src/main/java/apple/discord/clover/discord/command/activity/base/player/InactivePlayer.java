package apple.discord.clover.discord.command.activity.base.player;

import apple.discord.clover.api.base.request.TimeResolution;
import apple.discord.clover.discord.DiscordModule;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.jetbrains.annotations.Nullable;

public abstract class InactivePlayer {


    protected final WynnGuildMember guildMember;
    protected final Map<InactivePlayerQueryPlaytime, Future<Duration>> playtimeQueries = new HashMap<>();

    public InactivePlayer(WynnGuildMember guildMember) {
        this.guildMember = guildMember;
    }

    public abstract Instant getLastJoin();

    public abstract String getName();

    public Duration getInactiveDuration() {
        Instant lastJoin = getLastJoin();
        if (lastJoin == null) return null;
        return Duration.between(lastJoin, Instant.now());
    }

    public String getGuildRank() {
        return guildMember == null ? "???" : guildMember.rank;
    }

    @Nullable
    public Duration getPlaytime(TimeResolution resolution, int termsAfter) {
        InactivePlayerQueryPlaytime query = new InactivePlayerQueryPlaytime(resolution, termsAfter);
        try {
            return this.playtimeQueries.computeIfAbsent(query, this::asyncQueryPlaytime).get();
        } catch (InterruptedException | ExecutionException e) {
            DiscordModule.get().logger().error("Future was interrupted", e);
            return null;
        }
    }

    private ListenableFuture<Duration> asyncQueryPlaytime(InactivePlayerQueryPlaytime query) {
        return Futures.submit(() -> this.queryPlaytime(query), Executors.newSingleThreadExecutor());
    }

    @Nullable
    protected abstract Duration queryPlaytime(InactivePlayerQueryPlaytime query);
}
