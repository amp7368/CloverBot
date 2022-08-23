package apple.discord.clover.discord;

import apple.discord.acd.ACD;
import apple.discord.clover.CloverBot;
import apple.discord.clover.CloverConfig;
import apple.discord.clover.discord.inactivity.CommandInactivity;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.event.Level;

import javax.security.auth.login.LoginException;

public class DiscordBot extends ListenerAdapter {
    public static ACD ACD;
    public static JDA client;

    public static void load() throws LoginException {

        CloverBot.log("DiscordBot starting", Level.INFO);
        JDABuilder builder = JDABuilder.createLight(CloverConfig.get().getToken());
        client = builder.build();
        client.getPresence().setPresence(Activity.playing("Slash commands!"), false);
        ACD = new ACD("c!", client);
        new CommandInactivity(ACD);
//        new CommandStats(ACD);
//        new CommandSuggest(ACD);
//        new CommandHelp(ACD);
//        new WatchGuildCommand(ACD);
//        new ManageServerCommand(ACD);
//        new LinkAccountCommand(ACD);
//        new CommandChangelog(ACD);
        ACD.updateCommands();
        CloverBot.log("DiscordBot started", Level.INFO);
    }
}
