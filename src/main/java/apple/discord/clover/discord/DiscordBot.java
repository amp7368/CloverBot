package apple.discord.clover.discord;

import apple.discord.clover.discord.inactivity.CommandInactivity;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import discord.util.dcf.DCF;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class DiscordBot extends AppleModule {


    public static DCF dcf;

    public static void load() {
        JDABuilder builder = JDABuilder.createLight(DiscordConfig.get().getToken());
        JDA client = builder.build();
        client.getPresence().setPresence(Activity.playing("Slash commands!"), false);
        dcf = new DCF(client);
        dcf.commands().addCommand(new CommandInactivity());
        dcf.commands().updateCommands();
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        return List.of(configJson(DiscordConfig.class, "CloverConfig"));
    }

    @Override
    public String getName() {
        return "Discord";
    }
}
