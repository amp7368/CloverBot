package apple.discord.clover.discord.util;

import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildHeader;
import apple.utilities.util.Pretty;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.Nullable;

public class ArgumentUtils {

    @Nullable
    public static WynnGuildHeader getWynnGuild(SlashCommandInteraction event, String guildName) {
        List<WynnGuildHeader> guildMatches = WynnDatabase.get().getFromGuildName(guildName);
        WynnGuildHeader wynnGuild;
        if (guildMatches.size() == 1) {
            wynnGuild = guildMatches.get(0);
        } else if (guildMatches.isEmpty()) {
            event.reply(String.format("The guild '%s' was not found", guildName)).setEphemeral(true).queue();
            return null;
        } else {
            event.reply(Pretty.truncate(String.format("Pick the following guild that matches:\n%s",
                guildMatches.stream()
                    .map(guild -> String.format("%s [%s]", guild.name, guild.prefix))
                    .collect(Collectors.joining("\n"))
            ), 2000)).setEphemeral(true).queue();
            return null;
        }
        return wynnGuild;
    }
}
