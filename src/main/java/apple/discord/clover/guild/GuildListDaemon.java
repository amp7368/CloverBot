package apple.discord.clover.guild;

import apple.discord.clover.CloverBot;
import apple.discord.clover.util.Links;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynncraftGuildListResponse;
import apple.discord.clover.wynncraft.WynncraftService;
import apple.utilities.lamdas.daemon.AppleDaemon;
import apple.utilities.request.AppleJsonFromURL;
import apple.utilities.util.ExceptionUnpackaging;
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
        AppleJsonFromURL<WynncraftGuildListResponse> fromURL = new AppleJsonFromURL<>(Links.GUILD_LIST,
            WynncraftGuildListResponse.class);
        WynncraftService.get().taskCreator().accept(fromURL, response -> WynnDatabase.get().setGuilds(response.getGuilds()))
            .complete();
    }

    @Override
    public void error(Exception e) {
        CloverBot.get().logger().error("Exception in guild list daemon" + "\n" + ExceptionUnpackaging.getStackTrace(e));
    }
}
