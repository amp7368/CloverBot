package apple.discord.clover.wynncraft;

import apple.discord.clover.CloverBot;
import apple.discord.clover.util.FileIOService;
import apple.discord.clover.wynncraft.stats.player.WynnInactivePlayer;
import apple.utilities.database.ajd.AppleAJD;
import apple.utilities.database.ajd.AppleAJDTyped;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class WynnPlayerDatabase {

    private static final Map<UUID, WynnInactivePlayer> players = new HashMap<>();
    private static final Map<String, WynnPlayerInactivitySaveable> dbs = new HashMap<>();
    private static WynnPlayerDatabase instance;
    private static AppleAJDTyped<WynnPlayerInactivitySaveable> manager;

    public WynnPlayerDatabase() {
        instance = this;
        loadNow();
    }

    public static WynnPlayerDatabase get() {
        return instance;
    }


    public File getDBFolder() {
        return WynncraftModule.get().getFile("inactive", "player");
    }

    public void loadNow() {
        manager = AppleAJD.createTyped(WynnPlayerInactivitySaveable.class, getDBFolder(), FileIOService.get().taskCreator());
        @NotNull Collection<WynnPlayerInactivitySaveable> loadedDbs = manager.loadFolderNow();
        for (WynnPlayerInactivitySaveable db : loadedDbs) {
            synchronized (players) {
                players.putAll(db.getPlayers());
            }
        }
        CloverBot.get().logger().info("Wynn PlayerActivity DB loaded");
    }

    public void addPlayer(WynnInactivePlayer player) {
        synchronized (players) {
            players.put(player.getUUID(), player);
            String key = player.getUUID().toString().substring(0, 2);
            WynnPlayerInactivitySaveable savable = dbs.computeIfAbsent(key, WynnPlayerInactivitySaveable::new);
            savable.put(player);
            manager.saveInFolderNow(savable);
        }
    }

    public void getPlayer(UUID uuid, Consumer<WynnInactivePlayer> callback) {
        synchronized (players) {
            WynnInactivePlayer player = players.get(uuid);
            if (player == null) {
                WynncraftRatelimit.queuePlayer(TaskPriorityCommon.LOWEST, uuid, p -> {
                    callback.accept(p == null ? null : p.toWynnInactivePlayer());
                });
            } else {
                callback.accept(player);
            }
        }
    }

    public void updatePlayer(UUID uuid, Consumer<WynnInactivePlayer> callback) {
        WynncraftRatelimit.queuePlayer(TaskPriorityCommon.LOWEST, uuid, p -> {
            callback.accept(p == null ? null : p.toWynnInactivePlayer());
        });
    }
}
