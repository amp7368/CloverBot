package apple.discord.clover.database.player.guild;

import apple.discord.clover.database.CloverDatabase;
import apple.discord.clover.database.player.guild.query.QDGuild;
import apple.discord.clover.service.ServiceModule;
import apple.discord.clover.service.guild.GuildListService;
import apple.discord.clover.wynncraft.overview.guild.WynncraftGuildListEntry;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import io.ebean.DB;
import io.ebean.SqlUpdate;
import io.ebean.Transaction;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;

public class GuildStorage {

    private static final String SAVE_GUILD_UNIQUE_NAME = """
        INSERT INTO guild(created, name, tag, id, is_active)
        SELECT NULL, :guild_name, :guild_tag, gen_random_uuid(), TRUE
        WHERE NOT EXISTS(SELECT * FROM guild WHERE name = :guild_name AND is_active IS TRUE);
        """;

    public synchronized static DGuild findOrCreate(String name) {
        List<DGuild> guilds = new QDGuild().where()
            .name.eq(name)
            .isActive.isTrue()
            .findList();
        if (guilds.isEmpty()) {
            GuildListService.queueGuild(name);
            return null;
        }
        if (guilds.size() > 1) {
            CloverDatabase.get().logger().error("DGuild {} has multiple active guild entries", name);
        }
        return guilds.get(0);
    }

    public static List<String> findUnloaded() {
        return new QDGuild()
            .select(QDGuild.alias().name)
            .where()
            .tag.isNull()
            .isActive.isTrue()
            .findSingleAttributeList();
    }

    public static synchronized void save(WynnGuild wynnGuild) {
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

    public synchronized static void setActiveGuilds(WynncraftGuildListEntry[] currentlyActive) {
        QDGuild a = QDGuild.alias();
        Set<WynncraftGuildListEntry> toDeactivateGuilds = new HashSet<>(new QDGuild()
            .select(a.name, a.tag)
            .where().isActive.isTrue()
            .findStream()
            .map(WynncraftGuildListEntry::new)
            .toList());

        try (Transaction transaction = DB.beginTransaction()) {
            transaction.setBatchMode(true);
            transaction.setBatchSize(toDeactivateGuilds.size() * 2);

            List<WynncraftGuildListEntry> toActivate = Stream.of(currentlyActive)
                .filter(Predicate.not(toDeactivateGuilds::remove))
                .toList();
            toDeactivateGuilds.forEach(guild -> setInactive(guild.getName(), transaction));

            if (!toActivate.isEmpty()) {
                SqlUpdate saveGuildUpdate = DB.sqlUpdate(SAVE_GUILD_UNIQUE_NAME);
                for (WynncraftGuildListEntry wynncraftGuildListEntry : toActivate) {
                    saveGuild(saveGuildUpdate, wynncraftGuildListEntry);
                }

                saveGuildUpdate.executeBatch();
            }
            transaction.commit();
        }
    }

    private static void saveGuild(SqlUpdate saveGuildUpdate, WynncraftGuildListEntry guild) {
        saveGuildUpdate
            .setParameter("guild_name", guild.getName())
            .setParameter("guild_tag", guild.getTag())
            .addBatch();
        GuildListService.queueGuild(guild.getName());
    }

    private static void setInactive(String guildName, Transaction transaction) {
        QDGuild a = QDGuild.alias();
        new QDGuild().usingTransaction(transaction)
            .where()
            .name.eq(guildName)
            .isActive.isTrue()
            .asUpdate().set(a.isActive, false).update();
    }

    public static DGuild findById(UUID guildId) {
        return DB.find(DGuild.class, guildId);
    }
}
