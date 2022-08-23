package apple.discord.clover.discord.inactivity;

import apple.discord.acd.ACD;
import apple.discord.acd.slash.base.ACDBaseCommand;
import apple.discord.acd.slash.base.ACDSlashCommand;
import apple.discord.acd.slash.options.SlashOptionDefault;
import apple.discord.acd.slash.runner.ACDSlashMethodCommand;
import apple.discord.clover.discord.util.ArgumentUtils;
import apple.discord.clover.wynncraft.guild.WynnGuildHeader;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.Nullable;

@ACDBaseCommand(alias = "activity", description = "Create an activity report")
public class CommandInactivity extends ACDSlashCommand {
    public CommandInactivity(ACD acd) {
        super(acd);
    }

    @ACDSlashMethodCommand
    public void inactivityV2(SlashCommandInteractionEvent event,
                             @SlashOptionDefault(
                                     optionType = OptionType.STRING,
                                     name = "guild",
                                     description = "The guild to form the report of"
                             ) String guildName) {
        @Nullable Guild discordGuild = event.getGuild();
        WynnGuildHeader wynnGuild = ArgumentUtils.getWynnGuild(event, guildName, event.getUser(), discordGuild);
        if (wynnGuild == null) return;
        GuiInactivity gui = new GuiInactivity(acd, event, wynnGuild);
        MessageInactivityProgress progressPage = new MessageInactivityProgress(gui, () -> gui.addSubPage(new MessageInactivity(gui)));
        gui.addPage(progressPage);
        gui.makeFirstMessage();
    }
}