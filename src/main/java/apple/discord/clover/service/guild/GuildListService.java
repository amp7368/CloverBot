package apple.discord.clover.service.guild;

import apple.discord.clover.database.player.guild.GuildStorage;
import apple.discord.clover.service.base.DaemonService;
import apple.discord.clover.wynncraft.WynnResponse;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.discord.clover.wynncraft.overview.guild.WynncraftGuildListEntry;
import apple.discord.clover.wynncraft.overview.guild.WynncraftGuildListResponse;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import discord.util.dcf.util.TimeMillis;
import okhttp3.CacheControl;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class GuildListService extends DaemonService<WynncraftGuildListResponse> {

    private static final Builder GUILD_LIST_REQUEST = new Builder().get()
        .url(WynncraftApi.GUILD_LIST)
        .cacheControl(CacheControl.FORCE_NETWORK);


    public GuildListService() {
        queueStart();
    }

    public static void queueGuild(String guild) {
        WynncraftRatelimit.queueGuild(TaskPriorityCommon.HIGH, guild, c -> {});
    }


    @Override
    protected void start() {
        // TODO make next line synchronous
        GuildStorage.findUnloaded().forEach(GuildListService::queueGuild);
        GuildListConfig.get().sleepIfLastQueryRecent(normalInterval());
        super.start();
    }

    @Override
    protected String name() {
        return "GuildListService";
    }

    @Override
    protected void updateLastQuery() {
        GuildListConfig.get().updateLastQuery();
    }

    @Override
    protected boolean acceptResponse(WynnResponse<WynncraftGuildListResponse> response) {
        WynncraftGuildListResponse data = response.data();
        if (data == null || !data.hasGuilds()) return false;
        GuildStorage.setActiveGuilds(data.getGuilds());
        return true;
    }

    @Override
    protected Builder request() {
        return GUILD_LIST_REQUEST;
    }

    @Override
    protected WynncraftGuildListResponse deserialize(Response response) {
        WynncraftGuildListEntry[] guilds = WynncraftModule.gson()
            .fromJson(response.body().charStream(), WynncraftGuildListEntry[].class);
        return new WynncraftGuildListResponse(guilds);
    }

    @Override
    protected long normalInterval() {
        return TimeMillis.minToMillis(60);
    }
}
