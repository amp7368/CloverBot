package apple.discord.clover.discord.autocomplete;

import apple.discord.clover.database.player.PlayerStorage;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.Choice;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class PlayerAutoComplete extends CloverAutoComplete {

    public PlayerAutoComplete() {
        super("player");
    }

    @Override
    public void autoComplete(@NotNull CommandAutoCompleteInteractionEvent event, String arg) {
        List<Choice> choices = PlayerStorage.findLikePlayer(arg, OptionData.MAX_CHOICES)
            .stream()
            .map(player -> new Choice(player.username, player.username))
            .toList();
        event.replyChoices(choices).queue();
    }
}
