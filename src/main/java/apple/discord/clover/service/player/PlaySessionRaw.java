package apple.discord.clover.service.player;

import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import org.jetbrains.annotations.Nullable;

public record PlaySessionRaw(DLoginQueue login, WynnPlayer response) {

    public @Nullable WynnPlayer getPlayer() {
        return response;
    }
}
