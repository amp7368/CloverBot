package apple.discord.clover.wynncraft.run;

import apple.discord.clover.database.guild.GuildStorage;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.WynncraftUrls;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.utilities.request.AppleJsonFromURL;
import apple.utilities.threading.service.priority.AsyncTaskPriority;
import apple.utilities.threading.service.priority.TaskHandlerPriority;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import com.google.common.util.concurrent.Futures;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public class WynncraftOldRatelimit {

    private static final TaskHandlerPriority api = new TaskHandlerPriority(300, 60, 200);

    public static void queueGuild(TaskPriorityCommon priority, String guild, Consumer<WynnGuild> runAfter) {
        String link = WynncraftUrls.guild(guild);
        AppleJsonFromURL<WynnGuild> task = new AppleJsonFromURL<>(link, WynnGuild.class,
            WynncraftModule.gson());
        api.taskCreator(new AsyncTaskPriority(priority))
            .accept(task, (g) -> {
                if (g != null) {
                    g.initialize();
                    Futures.submit(() -> GuildStorage.save(g), ForkJoinPool.commonPool());
                }
                runAfter.accept(g);
            }, f -> ServiceModule.get().logger().error(f));
    }

    public static void queuePlayer(TaskPriorityCommon priority, UUID guildMember, Consumer<@Nullable WynnPlayer> runAfter) {
        String link = WynncraftUrls.playerStats(guildMember);
        api
            .taskCreator(new AsyncTaskPriority(priority))
            .accept(new AppleJsonFromURL<>(link, WynnPlayer.class, WynncraftModule.gson()))
            .onSuccess(runAfter);
    }
}
