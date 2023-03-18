package apple.discord.clover.wynncraft.stats.player.meta;

import java.util.Date;

public class WynnPlayerMeta {

    public Date firstJoin;
    public Date lastJoin;
    public long playtime; //hours
    public WynnPlayerTag tag;

    public static class WynnPlayerTag {

        public boolean display;
        public String value;
    }
}
