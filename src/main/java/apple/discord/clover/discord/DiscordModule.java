package apple.discord.clover.discord;

import apple.discord.clover.discord.autocomplete.CloverAutoCompleteListener;
import apple.discord.clover.discord.command.activity.CommandActivity;
import apple.discord.clover.discord.command.bug.CommandBug;
import apple.discord.clover.discord.command.help.CommandHelp;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.factory.AppleConfigLike;
import discord.util.dcf.DCF;
import discord.util.dcf.DCFCommandManager;
import discord.util.dcf.DCFModalManager;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class DiscordModule extends AppleModule {


    public static DCF dcf;
    public static String INVITE_LINK = "https://discord.com/api/oauth2/authorize?client_id=616398849803681889&permissions=18496&scope"
        + "=applications.commands%20bot";
    private static DiscordModule instance;

    public DiscordModule() {
        instance = this;
    }

    public static DiscordModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        JDA client = JDABuilder.createLight(DiscordConfig.get().getToken()).build();
        client.getPresence().setPresence(Activity.playing("Slash commands!"), false);
        dcf = new DCF(client);

        DCFCommandManager commands = dcf.commands();
        commands.addCommand(new CommandActivity());
        commands.addCommand(new CommandHelp());
        commands.addCommand(new CommandBug());
        commands.updateCommands();

        DCFModalManager modals = dcf.modals();

        client.addEventListener(new CloverAutoCompleteListener());

        try {
            client.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        DiscordConfig.get().load();
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
