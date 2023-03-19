package apple.discord.clover.wynncraft.stats.guild;


import java.util.Date;

public class WynnGuild {

    public String name;
    public String prefix;
    public WynnGuildMember[] members;
    public long level;
    public Date created;

    public WynnGuildHeader toHeader() {
        return new WynnGuildHeader(name, prefix);
    }
}
