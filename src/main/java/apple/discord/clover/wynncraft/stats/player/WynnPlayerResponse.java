package apple.discord.clover.wynncraft.stats.player;

import org.jetbrains.annotations.Nullable;

public class WynnPlayerResponse {

    public WynnPlayer[] data;

    @Nullable
    public WynnPlayer getPlayer() {
        return this.data.length == 0 ? null : data[0];
    }
}
