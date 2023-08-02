package apple.discord.clover.discord.command.activity.base;

import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.MessageInactivityProgress;
import apple.discord.clover.discord.util.ArgumentUtils;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.Nullable;

public abstract class BaseActivitySubCommand extends DCFSlashSubCommand implements FindOption {

    public static final OptionData GUILD_OPTION = new OptionData(OptionType.STRING, "guild",
        "The guild to create the activity report of", true);

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        @Nullable String guildName = findOption(event, "guild", OptionMapping::getAsString);
        if (guildName == null) return;
        DGuild wynnGuild = ArgumentUtils.getWynnGuild(event, guildName);
        if (wynnGuild == null) return;

        GuiInactivity gui = new GuiInactivity(dcf, event::reply, wynnGuild);
        MessageInactivityProgress progressPage = new MessageInactivityProgress(gui, () -> onProgressCompletion(gui));
        gui.addPage(progressPage);
        gui.send();
    }


    protected abstract void onProgressCompletion(GuiInactivity gui);
}
