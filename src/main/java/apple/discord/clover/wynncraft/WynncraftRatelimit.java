package apple.discord.clover.wynncraft;

import apple.discord.clover.database.player.guild.GuildStorage;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.discord.clover.wynncraft.stats.player.WynnPlayerResponse;
import apple.utilities.request.AppleJsonFromURL;
import apple.utilities.threading.service.priority.AsyncTaskPriority;
import apple.utilities.threading.service.priority.TaskHandlerPriority;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import com.google.common.util.concurrent.Futures;
import discord.util.dcf.util.TimeMillis;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WynncraftRatelimit {

    private static final TaskHandlerPriority network = legacyApi();
    private static final TaskHandlerPriority guild = legacyApi();
    private static final TaskHandlerPriority player = playerApi();

    public static TaskHandlerPriority getNetwork() {
        return network;
    }

    public static TaskHandlerPriority getGuild() {
        return guild;
    }

    public static TaskHandlerPriority getPlayer() {
        return player;
    }

    /**
     * <a href="https://docs.wynncraft.com/Player-API">Player Resource</a>
     * Rate limit 750 requests / 30 minutes
     */
    private static TaskHandlerPriority playerApi() {
        return createResource(750, (int) TimeMillis.minToMillis(30));
    }


    /**
     * <a href="https://docs.wynncraft.com">Rate Limit</a>
     * Rate limit legacy at 1200 requests / 20 minutes for legacy
     */
    private static TaskHandlerPriority legacyApi() {
        return createResource(1000, (int) TimeMillis.minToMillis(20));
    }

    @NotNull
    private static TaskHandlerPriority createResource(int tasks, int time) {
        return new TaskHandlerPriority(tasks, time, time / tasks);
    }


    public static void queueGuild(TaskPriorityCommon priority, String guild, Consumer<WynnGuild> runAfter) {
        AppleJsonFromURL<WynnGuild> task = new AppleJsonFromURL<>(String.format(WynncraftApi.GUILD_STATS, guild), WynnGuild.class,
            WynncraftModule.gson());
        getGuild().taskCreator(new AsyncTaskPriority(priority)).accept(task).onSuccess((g) -> {
            if (g != null)
                Futures.submit(() -> GuildStorage.save(g), ForkJoinPool.commonPool());
            runAfter.accept(g);
        });
    }

    public static void queuePlayer(TaskPriorityCommon priority, UUID guildMember, Consumer<@Nullable WynnPlayer> runAfter) {
        String link = String.format(WynncraftApi.PLAYER_STATS, guildMember);
        Consumer<WynnPlayerResponse> consumeResponse = (res) -> {
            WynnPlayer player = res == null ? null : res.getPlayer();
            runAfter.accept(player);
        };
        getPlayer()
            .taskCreator(new AsyncTaskPriority(priority))
            .accept(new AppleJsonFromURL<>(link, WynnPlayerResponse.class, WynncraftModule.gson()))
            .onSuccess(consumeResponse);
    }
}
