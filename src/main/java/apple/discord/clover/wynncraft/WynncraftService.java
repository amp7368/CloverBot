package apple.discord.clover.wynncraft;

import apple.discord.clover.util.Links;
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

public class WynncraftService {

    // rate limit with 429
    private static final TaskHandlerPriority instance = new TaskHandlerPriority(600 / 3, (int) TimeMillis.minToMillis(10), 350);

    @NotNull
    private static Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm'Z'").create();
    }

    public static TaskHandlerPriority get() {
        return instance;
    }


    public static void queueGuild(TaskPriorityCommon priority, String guild, Consumer<WynnGuild> runAfter) {
        AppleJsonFromURL<WynnGuild> task = new AppleJsonFromURL<>(String.format(Links.GUILD_STATS, guild), WynnGuild.class, gson());
        instance.taskCreator(new AsyncTaskPriority(priority)).accept(task).onSuccess(runAfter);
    }

    public static void queuePlayer(TaskPriorityCommon priority, String guildMember, Consumer<@Nullable WynnPlayer> runAfter) {
        String link = String.format(Links.PLAYER_STATS, guildMember);
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
        instance.taskCreator(new AsyncTaskPriority(priority)).accept(task).onSuccess(consumeResponse);
    }

}
