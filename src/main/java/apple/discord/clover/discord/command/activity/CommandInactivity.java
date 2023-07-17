package apple.discord.clover.discord.command.activity;

import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.discord.command.activity.pages.MessageInactivity;
import apple.discord.clover.discord.util.ArgumentUtils;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.Nullable;

public class CommandInactivity extends DCFSlashCommand implements FindOption {

    private static final Choice HISTORY = new Choice("History", "history");
    private static final Choice LAST_JOIN = new Choice("Last Join", "last_join");

    @Override
    public SlashCommandData getData() {
        OptionData kind = new OptionData(OptionType.STRING,
            "kind",
            "The kind of information contained in the report",
            false,
            true)
            .addChoices(HISTORY, LAST_JOIN);
        return Commands.slash("activity", "Create an activity report")
            .addOption(OptionType.STRING, "guild", "The guild to create the activity report of", true)
            .addOptions(kind);
    }


    public void onCommand(SlashCommandInteractionEvent event) {
        @Nullable String guildName = findOption(event, "guild", OptionMapping::getAsString);
        if (guildName == null) return;
        DGuild wynnGuild = ArgumentUtils.getWynnGuild(event, guildName);
        if (wynnGuild == null) return;

        GuiInactivity gui = new GuiInactivity(dcf, event::reply, wynnGuild);
        MessageInactivityProgress progressPage = new MessageInactivityProgress(gui, () -> gui.addSubPage(new MessageInactivity(gui)));
        gui.addPage(progressPage);
        gui.send();
    }
}