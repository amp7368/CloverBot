package apple.discord.clover.discord.command.player.history;

import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandPlayerHistory extends DCFSlashSubCommand implements FindOption {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("history", "Create a report of one player's playtime")
            .addOption(OptionType.STRING, "player", "The player to create a report of");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent slashCommandInteractionEvent) {

    }
}
