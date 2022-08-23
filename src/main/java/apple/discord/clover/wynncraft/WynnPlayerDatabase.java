package apple.discord.clover.wynncraft;

import apple.discord.clover.CloverBot;
import apple.discord.clover.util.FileIOService;
import apple.discord.clover.wynncraft.player.WynnInactivePlayer;
import apple.utilities.database.ajd.AppleAJDTypedImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.event.Level;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class WynnPlayerDatabase {
    private static final HashMap<UUID, WynnInactivePlayer> players = new HashMap<>();
    private static final HashMap<String, WynnPlayerInactivitySaveable> dbs = new HashMap<>();
    private static WynnPlayerDatabase instance;
    private static AppleAJDTypedImpl<WynnPlayerInactivitySaveable> manager;

    public WynnPlayerDatabase() {
        instance = this;
        loadNow();
    }

    public static WynnPlayerDatabase get() {
        return instance;
    }


    public static File getDBFolder() {
        return CloverBot.getFolder("wynncraft", "inactive", "player");
    }

    public static void loadNow() {
        manager = new AppleAJDTypedImpl<>(getDBFolder(), WynnPlayerInactivitySaveable.class, FileIOService.get());
        @NotNull Collection<WynnPlayerInactivitySaveable> loadedDbs = manager.loadAllNow(FileIOService.get());
        for (WynnPlayerInactivitySaveable db : loadedDbs) {
            synchronized (players) {
                players.putAll(db.getPlayers());
            }
        }
        CloverBot.log("Wynn PlayerActivity DB loaded", Level.INFO);
    }

    public void addPlayer(WynnInactivePlayer player) {
        synchronized (players) {
            players.put(player.getUUID(), player);
            String key = player.getUUID().toString().substring(0, 2);
            WynnPlayerInactivitySaveable saveable = dbs.computeIfAbsent(key, (k) -> new WynnPlayerInactivitySaveable(key));
            saveable.put(player);
            manager.save(saveable);
        }
    }

    public void getPlayer(UUID uuid, Consumer<WynnInactivePlayer> callback) {
        synchronized (players) {
            WynnInactivePlayer player = players.get(uuid);
            if (player == null) {
                WynncraftService.queuePriority(WynnRequestPriority.LAZY, uuid.toString(), p -> {
                    callback.accept(p == null ? null : p.toWynnInactivePlayer());
                });
            } else {
                callback.accept(player);
            }
        }
    }

    public void updatePlayer(UUID uuid, Consumer<WynnInactivePlayer> callback) {
        WynncraftService.queuePriority(WynnRequestPriority.LAZY, uuid.toString(), p -> {
            callback.accept(p == null ? null : p.toWynnInactivePlayer());
        });
    }
}
