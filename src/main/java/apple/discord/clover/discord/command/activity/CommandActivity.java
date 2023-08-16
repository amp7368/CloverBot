package apple.discord.clover.discord.command.activity;

import apple.discord.clover.discord.command.activity.history.HistorySubCommand;
import apple.discord.clover.discord.command.activity.last_join.CommandLastJoin;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandActivity extends DCFSlashCommand implements FindOption {


    @Override
    public SlashCommandData getData() {
        return Commands.slash("activity", "Create an activity report");
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new HistorySubCommand(), new CommandLastJoin());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
    }
}