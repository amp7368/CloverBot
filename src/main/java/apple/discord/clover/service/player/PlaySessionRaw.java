package apple.discord.clover.service.player;

import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.discord.clover.wynncraft.stats.player.WynnPlayerResponse;
import org.jetbrains.annotations.Nullable;

public record PlaySessionRaw(DLoginQueue login, WynnPlayerResponse response) {

    public @Nullable WynnPlayer getPlayer() {
        if (response == null) return null;
        return this.response.getPlayer();
    }
}
