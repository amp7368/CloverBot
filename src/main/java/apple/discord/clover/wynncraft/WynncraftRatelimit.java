package apple.discord.clover.wynncraft;

import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.discord.clover.wynncraft.stats.player.WynnPlayerResponse;
import apple.utilities.request.AppleJsonFromURL;
import apple.utilities.threading.service.priority.AsyncTaskPriority;
import apple.utilities.threading.service.priority.TaskHandlerPriority;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import discord.util.dcf.util.TimeMillis;
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
        return createResource(700, (int) TimeMillis.minToMillis(30));
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

    @NotNull
    public static Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm'Z'").create();
    }


    // todo
    public static void queueGuild(TaskPriorityCommon priority, String guild, Consumer<WynnGuild> runAfter) {
        AppleJsonFromURL<WynnGuild> task = new AppleJsonFromURL<>(String.format(WynncraftApi.GUILD_STATS, guild), WynnGuild.class,
            gson());
        WynncraftRatelimit.guild.taskCreator(new AsyncTaskPriority(priority)).accept(task).onSuccess(runAfter);
    }

    // todo
    public static void queuePlayer(TaskPriorityCommon priority, String guildMember, Consumer<@Nullable WynnPlayer> runAfter) {
        String link = String.format(WynncraftApi.PLAYER_STATS, guildMember);
        AppleJsonFromURL<WynnPlayerResponse> task = new AppleJsonFromURL<>(link, WynnPlayerResponse.class, gson());
        Consumer<WynnPlayerResponse> consumeResponse = (res) -> {
            if (res == null) {
                runAfter.accept(null);
                return;
            }
            WynnPlayer player = res.getPlayer();
            WynnDatabase.get().addMember(player);
            runAfter.accept(player);
        };
        guild.taskCreator(new AsyncTaskPriority(priority)).accept(task).onSuccess(consumeResponse);
    }
}
