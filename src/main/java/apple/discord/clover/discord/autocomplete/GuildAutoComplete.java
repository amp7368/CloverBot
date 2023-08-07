package apple.discord.clover.discord.autocomplete;

import apple.discord.clover.database.player.guild.GuildStorage;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import org.jetbrains.annotations.NotNull;

public class GuildAutoComplete extends CloverAutoComplete {

    public GuildAutoComplete() {
        super("guild");
    }

    @Override
    public void autoComplete(@NotNull CommandAutoCompleteInteractionEvent event, String arg) {
        List<Choice> choices = GuildStorage.findPartial(arg)
            .stream()
            .map(guild -> new Choice(guild.getName(), guild.getTag()))
            .toList();
        event.replyChoices(choices).queue();
    }
}
