package apple.discord.clover.wynncraft;

import apple.discord.clover.util.FileIOService;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildHeader;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDInst;
import apple.utilities.fileio.serializing.FileIOJoined;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WynnDatabase {

    private static final File GUILD_FOLDER = WynncraftModule.get().getFile("guildList");
    private static AppleAJDInst<WynnDatabase> manager;

    private final Set<String> guildsRequestedToBeUpdated = new HashSet<>();
    private final Map<String, WynnPlayer> members = new HashMap<>();
    private final Map<String, WynnGuildHeader> guilds = new HashMap<>(20000);

    public static WynnDatabase get() {
        return manager.getInstance();
    }

    public static void load() {
        File file = WynncraftModule.get().getFile("WynnDatabase.json");
        manager = AppleAJD.createInst(WynnDatabase.class, file, FileIOService.get().taskCreator());
        manager.loadOrMake();
    }

    public List<WynnGuildHeader> getFromGuildName(String guildName) {
        synchronized (this) {
            List<WynnGuildHeader> matches = new ArrayList<>();
            for (WynnGuildHeader guild : guilds.values()) {
                if (guild.matchesTag(guildName)) {
                    matches.add(guild);
                }
            }
            if (!matches.isEmpty()) return matches;
            for (WynnGuildHeader guild : guilds.values()) {
                if (guild.matchesTagIgnoreCase(guildName)) {
                    matches.add(guild);
                }
            }
            if (!matches.isEmpty()) return matches;
            for (WynnGuildHeader guild : guilds.values()) {
                if (guild.matchesGuildNameExactly(guildName)) {
                    matches.add(guild);
                }
            }
            if (!matches.isEmpty()) return matches;
            for (WynnGuildHeader guild : guilds.values()) {
                if (guild.matchesGuildName(guildName)) {
                    matches.add(guild);
                }
            }
            if (!matches.isEmpty()) return matches;
            for (WynnGuildHeader guild : guilds.values()) {
                if (guild.matchesGuildNameIgnoreCase(guildName)) {
                    matches.add(guild);
                }
            }
            return matches;
        }
    }

    public void setGuilds(String[] guilds) {
        synchronized (this) {
            for (String guild : guilds) {
                WynnGuildHeader guildHeader = this.guilds.get(guild);
                if (guildHeader == null || guildHeader.prefix == null) {
                    this.guilds.put(guild, new WynnGuildHeader(guild));
                    if (guildsRequestedToBeUpdated.add(guild)) {
                        WynncraftRatelimit.queueGuild(TaskPriorityCommon.LOWER, guild, (wynnGuild) -> {
                            addGuild(wynnGuild);

                            HashMap<String, WynnGuildHeader> headersToSave = getHeaders();
                            File file = new File(GUILD_FOLDER, "guilds_all.json");
                            try {
                                FileIOJoined.get().saveJson(file, headersToSave, null);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            }
        }
    }

    private HashMap<String, WynnGuildHeader> getHeaders() {
        synchronized (this) {
            return new HashMap<>(get().guilds);
        }
    }

    private void addGuild(WynnGuild wynnGuild) {
        synchronized (this) {
            WynnGuildHeader header = wynnGuild.toHeader();
            guilds.put(header.name, header);
            guildsRequestedToBeUpdated.remove(header.name);
        }
    }

    public void addMember(WynnPlayer member) {
        synchronized (this) {
            members.put(member.uuid.toString(), member);
        }
        WynnPlayerDatabase.get().addPlayer(member.toWynnInactivePlayer());
    }

    @Nullable
    public WynnPlayer getPlayer(String uuid) {
        synchronized (this) {
            WynnPlayer member = members.get(uuid);
            if (member == null) return null;
            if (member.isOld()) {
                members.remove(uuid);
                return null;
            }
            return member;
        }
    }

    public int getSize() {
        synchronized (this) {
            return guilds.size();
        }
    }

    @NotNull
    public List<WynnPlayer> getPlayerMatches(String playerName) {
        synchronized (this) {
            List<WynnPlayer> matches = new ArrayList<>();
            List<WynnPlayer> badMatches = new ArrayList<>();
            for (WynnPlayer player : members.values()) {
                if (player.nameEquals(playerName)) {
                    return List.of(player);
                }
                if (player.nameContains(playerName)) {
                    matches.add(player);
                }
                if (player.nameOneCharOff(playerName)) {
                    if (matches.isEmpty()) badMatches.add(player);
                }
            }
            return matches.isEmpty() ? badMatches : matches;
        }
    }
}
