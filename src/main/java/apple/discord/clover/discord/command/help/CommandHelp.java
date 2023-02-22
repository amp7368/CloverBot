package apple.discord.clover.discord.command.help;

import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.slash.DCFSlashCommand;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandHelp extends DCFSlashCommand {

    private static final List<MessageEmbed> pages = List.of(createHomePage(), createContactPage());

    private static MessageEmbed createHomePage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Overview");
        embed.setDescription("CloverBot is a guild activity monitoring bot.");
        embed.addField("/activity basic [guild_name]", "Create a simple report of when player's last logged in", false);
        embed.addField("/activity history [guild_name]", "Create a simple report of when player's last logged in", false);
        embed.addField("/pactivity history [guild_name]", "Create a simple report of when player's last logged in", false);
        embed.addField("/bug", "Opens a modal to report a bug", false);
        embed.addField("/suggest", "Opens a modal to add a suggest or comment", false);
        return embed.build();
    }

    private static MessageEmbed createContactPage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Contact");
        embed.setDescription("""
            Developer contact: appleptr16#5054
            Discord server: https://discord.gg/ED8GRyHmpB
            Send any suggestions directly to appleptr16#5054 or using the /suggest command
            Report any bugs using the /bug command
            """);
        return embed.build();
    }

    @Override
    public SlashCommandData getData() {
        return Commands.slash("help", "Shows helpful message");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        DCFGui gui = new DCFGui(dcf, event::reply);
        for (MessageEmbed page : pages)
            gui.addPage(new HelpPage(gui, page));
        gui.send();
    }

}
