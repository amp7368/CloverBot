package apple.discord.clover.discord.util;

import apple.discord.clover.database.guild.DGuild;
import apple.discord.clover.database.guild.GuildStorage;
import apple.utilities.util.Pretty;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class ArgumentUtils {

    public static DGuild getWynnGuild(SlashCommandInteraction event, String guildName) {
        List<DGuild> guildMatches = GuildStorage.find(guildName);
        DGuild wynnGuild;
        if (guildMatches.size() == 1) {
            wynnGuild = guildMatches.get(0);
        } else if (guildMatches.isEmpty()) {
            event.reply(String.format("The guild '%s' was not found", guildName)).setEphemeral(true).queue();
            return null;
        } else {
            event.reply(Pretty.truncate(String.format("Pick the following guild that matches:\n%s",
                guildMatches.stream()
                    .map(guild -> String.format("%s [%s]", guild.getName(), guild.getTag()))
                    .collect(Collectors.joining("\n"))
            ), 2000)).setEphemeral(true).queue();
            return null;
        }
        return wynnGuild;
    }
}
