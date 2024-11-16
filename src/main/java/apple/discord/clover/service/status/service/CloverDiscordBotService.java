package apple.discord.clover.service.status.service;

import apple.discord.clover.database.meta.status.ServiceActivityType;
import apple.discord.clover.discord.DiscordBot;
import apple.discord.clover.service.status.CloverStatusService;
import java.time.Instant;
import net.dv8tion.jda.api.JDA.Status;

public class CloverDiscordBotService extends CloverStatusService {

    @Override
    protected void init() {
        markOffline(Instant.now(), true);
    }

    @Override
    protected void run() {
        Status status = DiscordBot.jda().getStatus();
        if (status == Status.CONNECTED) {
            markOnline(Instant.now(), false);
        } else {
            markOffline(Instant.now(), false);
        }
    }

    @Override
    protected ServiceActivityType getActivity() {
        return ServiceActivityType.DISCORD_BOT;
    }
}
