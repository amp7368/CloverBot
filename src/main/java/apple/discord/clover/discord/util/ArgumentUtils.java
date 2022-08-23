package apple.discord.clover.discord.util;

import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.guild.WynnGuildHeader;
import apple.utilities.util.Pretty;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ArgumentUtils {
    @Nullable
    public static WynnGuildHeader getWynnGuild(SlashCommandInteraction event, String guildName, User author, Guild discordGuild) {
        List<WynnGuildHeader> guildMatches = WynnDatabase.getFromGuildName(guildName);
        WynnGuildHeader wynnGuild;
        if (guildMatches.size() == 1) {
            wynnGuild = guildMatches.get(0);
        } else if (guildMatches.isEmpty()) {
            event.reply(String.format("The guild '%s' was not found", guildName)).queue();
            return null;
        } else {
            // else try it with guildName, but tell the user if nothing happens
            event.reply(Pretty.truncate(String.format("Pick the following guild that matches:\n%s",
                    guildMatches.stream()
                            .map(guild -> String.format("%s [%s]", guild.name, guild.prefix))
                            .collect(Collectors.joining("\n"))
            ), 2000)).queue();
            return null;
        }
        return wynnGuild;
    }
}
