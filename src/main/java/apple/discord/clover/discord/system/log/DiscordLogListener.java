package apple.discord.clover.discord.system.log;

import apple.discord.clover.CloverBot;
import apple.discord.clover.database.log.CommandLogApi;
import apple.discord.clover.database.log.DCommandLog;
import apple.discord.clover.discord.DiscordConfig;
import java.time.Duration;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class DiscordLogListener extends ListenerAdapter {

    public static final Duration DELAY = Duration.ofSeconds(4);

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CloverBot.get().schedule(new LogCommand(event), DELAY);
    }


    private record LogCommand(SlashCommandInteractionEvent event) implements Runnable {

        @Override
        public void run() {
            DCommandLog log = CommandLogApi.log(event);

            if (DiscordConfig.get().isDevServer(event.getGuild())) return;
            TextChannel channel = DiscordConfig.get().getLogChannel();
            channel.sendMessageEmbeds(log.toMessage()).queue();
        }
    }
}
