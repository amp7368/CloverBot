package apple.discord.clover.discord.command.config;

import apple.discord.clover.discord.command.config.raid.ConfigRaidLogCommand;
import discord.util.dcf.slash.DCFSlashCommand;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class ConfigCommand extends DCFSlashCommand {

    @Override
    public SlashCommandData getData() {
        return Commands.slash("config", "Configure cloverbot for this server");
    }

    @Override
    public List<DCFSlashSubCommand> getSubCommands() {
        return List.of(new ConfigRaidLogCommand());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
    }
}
