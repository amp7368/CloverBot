package apple.discord.clover.service.guild;

import apple.discord.clover.database.player.guild.GuildStorage;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.utilities.threading.service.priority.TaskPriorityCommon;

public class GuildService {

    public static void load() {
        GuildStorage.findUnloaded().forEach(GuildService::queueGuild);
    }

    public static void queueGuild(String guild) {
        WynncraftRatelimit.queueGuild(TaskPriorityCommon.LOW, guild, GuildStorage::save);
    }
}
