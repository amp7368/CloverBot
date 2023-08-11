package apple.discord.clover.database.player.guild;

import apple.discord.clover.database.player.guild.query.QDGuild;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.service.guild.GuildService;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import io.ebean.DB;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;

public class GuildStorage {

    public static DGuild findOrCreate(String name) {
        DGuild guild = new QDGuild().where()
            .name.eq(name)
            .isActive.isTrue()
            .findOne();
        if (guild != null)
            return guild;
        guild = new DGuild(name);
        guild.save();
        GuildService.queueGuild(name);
        return guild;
    }

    public static List<String> findUnloaded() {
        return new QDGuild()
            .select(QDGuild.alias().name)
            .where()
            .tag.isNull()
            .isActive.isTrue()
            .findSingleAttributeList();
    }

    public static void save(WynnGuild wynnGuild) {
        DGuild guild = new QDGuild().where()
            .name.eq(wynnGuild.name)
            .isActive.isTrue()
            .findOne();
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

    public static List<DGuild> findPartial(String guildArg) {
        List<DGuild> guildsWithTag = new QDGuild().where()
            .tag.icontains(guildArg)
            .isActive.isTrue()
            .findList();
        List<DGuild> guilds = new ArrayList<>(guildsWithTag);
        guildsWithTag = new QDGuild().where()
            .name.icontains(guildArg)
            .isActive.isTrue()
            .findList();
        guilds.addAll(guildsWithTag);
        guilds.sort(findPartialGuildComparator(guildArg));
        return guilds;
    }

    private static Comparator<DGuild> findPartialGuildComparator(String guildArg) {
        return Comparator.comparing(DGuild::getName, Comparator.comparingInt(
            (guild) -> LevenshteinDetailedDistance.getDefaultInstance().apply(guildArg, guild).getDistance()));
    }

    public static List<DGuild> find(String guildArg) {
        List<DGuild> guild = new QDGuild().where()
            .tag.eq(guildArg)
            .isActive.isTrue()
            .findList();
        if (!guild.isEmpty()) return guild;
        guild = new QDGuild().where()
            .tag.ieq(guildArg)
            .isActive.isTrue()
            .findList();
        if (!guild.isEmpty()) return guild;
        guild = new QDGuild().where()
            .name.eq(guildArg)
            .isActive.isTrue()
            .findList();
        if (!guild.isEmpty()) return guild;
        guild = new QDGuild().where()
            .name.ieq(guildArg)
            .isActive.isTrue()
            .findList();
        return guild;
    }

    public static void setActiveGuilds(String[] guilds) {
        QDGuild a = QDGuild.alias();
        Set<String> toDeactivateGuilds = new HashSet<>(new QDGuild()
            .select(a.name)
            .where().isActive.isTrue()
            .findSingleAttributeList());
        List<String> toActivateGuilds = new ArrayList<>();
        for (String guild : List.of(guilds)) {
            if (!toDeactivateGuilds.remove(guild)) {
                toActivateGuilds.add(guild);
            }
        }
        toDeactivateGuilds.forEach(guild -> setActive(guild, false));
        toActivateGuilds.forEach(guild -> setActive(guild, true));
    }

    private static int setActive(String guild, boolean isActive) {
        QDGuild a = QDGuild.alias();
        return new QDGuild().where().name.eq(guild)
            .asUpdate().set(a.isActive, isActive).update();
    }

    public static DGuild findById(UUID guildId) {
        return DB.find(DGuild.class, guildId);
    }
}
