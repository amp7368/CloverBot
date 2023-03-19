package apple.discord.clover.wynncraft.overview.guild;

import apple.discord.clover.CloverBot;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.utilities.lamdas.daemon.AppleDaemon;
import apple.utilities.request.AppleJsonFromURL;
import apple.utilities.util.ExceptionUnpackaging;
import com.google.gson.Gson;
import discord.util.dcf.util.TimeMillis;

public class GuildListDaemon implements AppleDaemon {

    @Override
    public long getSleepTime() {
        return TimeMillis.DAY / 2;
    }

    @Override
    public void onPostInit() {
        CloverBot.get().logger().info("Daemon GuildList started");
    }

    @Override
    public void daemon() {
        AppleJsonFromURL<WynncraftGuildListResponse> fromURL = new AppleJsonFromURL<>(WynncraftApi.GUILD_LIST,
            WynncraftGuildListResponse.class, new Gson());
        WynncraftRatelimit.getGuild().taskCreator().accept(fromURL, response -> WynnDatabase.get().setGuilds(response.getGuilds()))
            .complete();
    }

    @Override
    public void error(Exception e) {
        CloverBot.get().logger().error("Exception in guild list daemon" + "\n" + ExceptionUnpackaging.getStackTrace(e));
    }
}
