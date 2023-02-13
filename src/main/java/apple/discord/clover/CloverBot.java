package apple.discord.clover;

import apple.discord.clover.discord.DiscordBot;
import apple.discord.clover.guild.GuildListDaemon;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.lib.modules.AppleModule;
import apple.lib.modules.ApplePlugin;
import java.util.List;

public class CloverBot extends ApplePlugin {

    private static CloverBot instance;

    public static void main(String[] args) {
        instance = new CloverBot();
        instance.start();
    }

    public static CloverBot get() {
        return instance;
    }

    @Override
    public void onEnable() {
        WynnDatabase.load();
        DiscordBot.load();
        new GuildListDaemon().start();
    }

    @Override
    public String getName() {
        return "CloverBot";
    }

    @Override
    public List<AppleModule> createModules() {
        return List.of(new WynncraftModule(), new DiscordBot());
    }
}
