package apple.discord.clover.discord.command.player;

import apple.discord.clover.discord.command.player.guild.CommandPlayerGuild;
import apple.discord.clover.discord.command.player.history.CommandPlayerHistory;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandPlayerActivity extends DCFSlashCommand implements FindOption {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("pactivity", "Create an activity report for a player");
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new CommandPlayerHistory(), new CommandPlayerGuild());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
    }
}
