package apple.discord.clover.guild;

import apple.discord.acd.MillisTimeUnits;
import apple.discord.clover.CloverBot;
import apple.discord.clover.util.Links;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynncraftGuildListResponse;
import apple.discord.clover.wynncraft.WynncraftService;
import apple.utilities.lamdas.daemon.AppleDaemon;
import apple.utilities.request.AppleJsonFromURL;
import apple.utilities.util.ExceptionUnpackaging;
import org.slf4j.event.Level;

public class GuildListDaemon implements AppleDaemon {
    @Override
    public long getSleepTime() {
        return MillisTimeUnits.DAY / 2;
    }

    @Override
    public void onPostInit() {
        CloverBot.log("Daemon GuildList started", Level.INFO);
    }

    @Override
    public void daemon() {
        WynncraftService.get().queue(new AppleJsonFromURL<>(Links.GUILD_LIST, WynncraftGuildListResponse.class), response -> {
            WynnDatabase.setGuilds(response.getGuilds());
        }).completeAndRun();
    }

    @Override
    public void error(Exception e) {
        CloverBot.log("Exception in guild list daemon" + "\n" + ExceptionUnpackaging.getStackTrace(e), Level.ERROR);
    }
}
