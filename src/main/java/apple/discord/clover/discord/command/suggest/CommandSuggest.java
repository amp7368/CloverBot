package apple.discord.clover.discord.command.suggest;

import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandSuggest extends DCFSlashCommand implements FindOption {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("suggest", "Suggest an idea to the developer")
            .addOption(OptionType.STRING, "description", "Summary of the suggestion", true)
            .addOption(OptionType.ATTACHMENT, "attachment", "Optional images", false);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionType description = event.getOption("description", OptionMapping::getType);

    }
}
