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
        embed.setDescription("""
            # Overview
            ### CloverBot is a activity reporting bot for Wynncraft.
            """);
        embed.addField("/activity last_join [guild]", "Create a report of when players in the guild last logged in", false);
        embed.addField("/activity history [guild]", "Create a report of playtime of players in the guild", false);
        embed.addField("/pactivity guild [player]", "Create a report of one player's guild history", false);
        embed.addField("/pactivity history [player]", "Create a report of one player's playtime", false);
        embed.addField("/bug [description] (attachment)", "Opens a modal to report a bug", false);
        embed.addField("/suggest", "Opens a modal to add a suggest or comment", false);
        return embed.build();
    }

    private static MessageEmbed createContactPage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Contact");
        embed.setDescription("""
            Developer's discord: `appleptr16`
            Discord server: https://discord.gg/ED8GRyHmpB
                        
            Direct any questions to `appleptr16`
            Send suggestions using the /suggest command
            Report bugs using the /bug command
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
