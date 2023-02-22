package apple.discord.clover.discord.command.activity;

import apple.discord.clover.discord.util.ArgumentUtils;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildHeader;
import discord.util.dcf.slash.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandInactivity extends DCFSlashCommand {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("activity", "Create an activity report")
            .addOption(OptionType.STRING, "guild", "The guild to create an activity report of", true);
    }


    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping guildNameOption = event.getOption("guild");
        if (guildNameOption == null) return;
        String guildName = guildNameOption.getAsString();
        WynnGuildHeader wynnGuild = ArgumentUtils.getWynnGuild(event, guildName);
        if (wynnGuild == null) return;
        GuiInactivity gui = new GuiInactivity(dcf, event::reply, wynnGuild);
        MessageInactivityProgress progressPage = new MessageInactivityProgress(gui, () -> gui.addSubPage(new MessageInactivity(gui)));
        gui.addPage(progressPage);
        gui.send();
    }
}