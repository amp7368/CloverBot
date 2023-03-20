package apple.discord.clover.database.guild;

import apple.discord.clover.database.guild.query.QDGuild;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.service.guild.GuildService;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import java.sql.Timestamp;
import java.util.List;

public class GuildStorage {

    public static DGuild findOrCreate(String name) {
        DGuild guild = new QDGuild().where().name.eq(name).findOne();
        if (guild != null)
            return guild;
        guild = new DGuild(name);
        guild.save();
        GuildService.queueGuild(name);
        return guild;
    }

    public static List<String> findUnloaded() {
        return new QDGuild().select(QDGuild.alias().name).where().tag.eq((String) null).findSingleAttributeList();
    }

    public static void save(WynnGuild wynnGuild) {
        DGuild guild = new QDGuild().where().name.eq(wynnGuild.name).findOne();
        if (guild == null)
            guild = new DGuild(wynnGuild.name);
        guild.setTag(wynnGuild.prefix);
        if (wynnGuild.created == null) {
            ServiceModule.get().logger().error("%s [%s] does not have a created date!".formatted(wynnGuild.name, wynnGuild.prefix));
            return;
        }
        guild.setCreated(Timestamp.from(wynnGuild.created.toInstant()));
        guild.save();
    }
}
