package apple.discord.clover.discord;

import apple.discord.clover.discord.command.activity.CommandActivity;
import apple.discord.clover.discord.command.help.CommandHelp;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import discord.util.dcf.DCF;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class DiscordModule extends AppleModule {


    public static DCF dcf;
    public static String INVITE_LINK = "https://discord.com/api/oauth2/authorize?client_id=616398849803681889&permissions=18496&scope"
        + "=applications.commands%20bot";

    @Override
    public void onEnable() {
        JDABuilder builder = JDABuilder.createLight(DiscordConfig.get().getToken());
        JDA client = builder.build();
        client.getPresence().setPresence(Activity.playing("Slash commands!"), false);
        dcf = new DCF(client);
        dcf.commands().addCommand(new CommandActivity());
        dcf.commands().addCommand(new CommandHelp());
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
