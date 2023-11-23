package apple.discord.clover.service.guild;

import apple.discord.clover.database.player.guild.GuildStorage;
import apple.discord.clover.service.base.DaemonService;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.discord.clover.wynncraft.overview.guild.WynncraftGuildListResponse;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import discord.util.dcf.util.TimeMillis;
import okhttp3.CacheControl;
import okhttp3.Request.Builder;
import okhttp3.Response;

public class GuildService extends DaemonService<WynncraftGuildListResponse> {

    private static final Builder GUILD_LIST_REQUEST = new Builder().get()
        .url(WynncraftApi.GUILD_LIST)
        .cacheControl(CacheControl.FORCE_NETWORK);


    public GuildService() {
        queueStart();
    }

    public static void queueGuild(String guild) {
        WynncraftRatelimit.queueGuild(TaskPriorityCommon.LOW, guild, c -> {});
    }


    @Override
    protected void start() {
        GuildStorage.findUnloaded().forEach(GuildService::queueGuild);
        GuildListConfig.get().sleepIfLastQueryRecent(normalInterval());
        super.start();
    }

    @Override
    protected void updateLastQuery() {
        GuildListConfig.get().updateLastQuery();
    }

    @Override
    protected void acceptResponse(WynncraftGuildListResponse response) {
        if (response == null || !response.hasGuilds()) return;
        throttle.incrementSuccess();
        GuildStorage.setActiveGuilds(response.getGuilds());
    }

    @Override
    protected Builder request() {
        return GUILD_LIST_REQUEST;
    }

    @Override
    protected WynncraftGuildListResponse deserialize(Response response) {
        return WynncraftModule.gson()
            .fromJson(response.body().charStream(), WynncraftGuildListResponse.class);
    }

    @Override
    protected long normalInterval() {
        return TimeMillis.minToMillis(5);
    }
}
