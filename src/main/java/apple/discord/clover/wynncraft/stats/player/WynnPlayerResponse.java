package apple.discord.clover.wynncraft.stats.player;

import org.jetbrains.annotations.Nullable;

public class WynnPlayerResponse {

    public WynnPlayer[] data;

    public long timestamp;

    @Nullable
    public WynnPlayer getPlayer() {
        if (this.data.length == 0) return null;
        WynnPlayer player = data[0];
        if (player != null) player.timeRetrieved = timestamp;
        return player;
    }
}
