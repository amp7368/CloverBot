package apple.discord.clover.discord.autocomplete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CloverAutoCompleteListener extends ListenerAdapter {

    private final Map<String, CloverAutoComplete> autoCompletes = new HashMap<>();

    public CloverAutoCompleteListener() {
        getAutoCompletes().forEach(auto -> autoCompletes.put(auto.getOptionName(), auto));
    }

    @NotNull
    private List<GuildAutoComplete> getAutoCompletes() {
        return List.of(new GuildAutoComplete());
    }

    @Override
    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
        String option = event.getFocusedOption().getName();
        CloverAutoComplete autoComplete;
        synchronized (autoCompletes) {
            autoComplete = autoCompletes.get(option);
        }
        if (autoComplete != null)
            autoComplete.autoComplete(event, option);
    }
}
