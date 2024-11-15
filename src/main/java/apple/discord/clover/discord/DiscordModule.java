package apple.discord.clover.discord;

import apple.discord.clover.CloverConfig;
import apple.discord.clover.discord.autocomplete.CloverAutoCompleteListener;
import apple.discord.clover.discord.command.activity.CommandActivity;
import apple.discord.clover.discord.command.bug.CommandBug;
import apple.discord.clover.discord.command.help.CommandHelp;
import apple.discord.clover.discord.command.player.CommandPlayerActivity;
import apple.discord.clover.discord.system.log.DiscordLogListener;
import apple.lib.modules.AppleModule;
import discord.util.dcf.DCF;
import discord.util.dcf.DCFCommandManager;
import java.time.ZoneId;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordModule extends AppleModule {


    public static final ZoneId TIME_ZONE = ZoneId.of("US/Eastern");
    public static DCF dcf;
    public static String INVITE_LINK = "https://discord.com/api/oauth2/authorize?client_id=616398849803681889&permissions=18496"
        + "&scope=applications.commands%20bot";
    private static DiscordModule instance;

    public DiscordModule() {
        instance = this;
    }

    public static DiscordModule get() {
        return instance;
    }

    @Override
    public void onEnable() {
        JDA jda = JDABuilder.createDefault(CloverConfig.getDiscord().token)
            .disableCache(CacheFlag.VOICE_STATE, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
            .build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dcf = new DCF(jda);
        DiscordBot.ready(dcf);

        jda.getPresence().setActivity(Activity.customStatus("Generating activity reports!"));
        CloverConfig.getDiscord().load();

        DCFCommandManager commands = dcf.commands();
        commands.addCommand(new CommandActivity());
        commands.addCommand(new CommandPlayerActivity());
        commands.addCommand(new CommandHelp());
        commands.addCommand(new CommandBug());
        commands.updateCommands();

        jda.addEventListener(new CloverAutoCompleteListener());
        jda.addEventListener(new DiscordLogListener());
    }

    @Override
    public String getName() {
        return "Discord";
    }
}
