package apple.discord.clover.discord.util;

import apple.discord.clover.api.base.request.FindPlayerFromString;
import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import java.util.function.Function;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.Nullable;

public interface FindOption extends SendMessage {

    @Nullable
    default <T> T findOption(SlashCommandInteractionEvent event, String optionName, Function<OptionMapping, T> getAs) {
        return findOption(event, optionName, getAs, true);
    }

    @Nullable
    default PlayerNameOrUUID findPlayer(SlashCommandInteractionEvent event) {
        String playerString = findOption(event, "player", OptionMapping::getAsString);
        if (playerString == null) return null;
        PlayerNameOrUUID player = FindPlayerFromString.find(playerString);
        if (player == null) {
            MessageEmbed msg = error("Could not find player '%s'".formatted(playerString));
            event.replyEmbeds(msg)
                .setEphemeral(true)
                .queue();
        }
        return player;
    }

    @Nullable
    default <T> T findOption(SlashCommandInteractionEvent event, String optionName, Function<OptionMapping, T> getAs,
        boolean isRequired) {
        OptionMapping option = event.getOption(optionName);
        if (option == null || getAs.apply(option) == null) {
            if (isRequired)
                this.missingOption(event, optionName);
            return null;
        }
        return getAs.apply(option);
    }

    default MessageEmbed missingOption(String option) {
        return error(String.format("'%s' is required", option));
    }

    default void missingOption(SlashCommandInteractionEvent event, String option) {
        event.replyEmbeds(missingOption(option)).setEphemeral(true).queue();
    }
}
