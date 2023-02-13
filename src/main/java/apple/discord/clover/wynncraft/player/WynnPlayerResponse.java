package apple.discord.clover.wynncraft.player;

public class WynnPlayerResponse {

    public WynnPlayer[] data;

    public WynnPlayer getPlayer() {
        return this.data.length == 0 ? null : data[0];
    }
}
