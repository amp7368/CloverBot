package apple.discord.clover;

import apple.discord.clover.discord.DiscordBot;
import apple.discord.clover.guild.GuildListDaemon;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynnPlayerDatabase;
import apple.utilities.util.ArrayUtils;
import apple.utilities.util.FileFormatting;
import org.slf4j.event.Level;

import javax.security.auth.login.LoginException;
import java.io.File;

public class CloverBot {
    public static void main(String[] args) throws LoginException {
        log("CloverBot starting", Level.INFO);
        CloverConfig.load();
        new WynnPlayerDatabase();
        WynnDatabase.loadDatabase();
        DiscordBot.load();
        new GuildListDaemon().start();
        log("CloverBot started", Level.INFO);
    }

    public static void log(String msg, Level lvl) {
        System.out.println(msg);
    }

    public static File getBuildFile(String... children) {
        return FileFormatting.fileWithChildren(getDbFolder(), children);
    }

    private static File getDbFolder() {
        return FileFormatting.getDBFolder(CloverBot.class);
    }

    public static File getFolder(String... children) {
        children = ArrayUtils.combine(new String[][]{new String[]{"data"}, children}, String[]::new);
        File file = getBuildFile(children);
        file.mkdirs();
        return file;
    }
}
