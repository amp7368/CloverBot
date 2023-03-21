package apple.discord.clover.database;

import apple.discord.clover.CloverBot;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.blacklist.DBlacklist;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.activity.run.DDungeonRun;
import apple.discord.clover.database.activity.run.DLevelupRun;
import apple.discord.clover.database.activity.run.DRaidRun;
import apple.discord.clover.database.activity.run.DSessionRunBase;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.guild.DGuild;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.primitive.IncrementalBigInt;
import apple.discord.clover.database.primitive.IncrementalFloat;
import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.database.primitive.IncrementalString;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.data.config.AppleConfig.Builder;
import apple.lib.modules.configs.factory.AppleConfigLike;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CloverDatabase extends AppleModule {

    private File databaseConfigFile;

    private static List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();
        // superclass
        entities.add(DSessionRunBase.class);
        // embedded
        entities.addAll(List.of(IncrementalInt.class, IncrementalBigInt.class, IncrementalFloat.class, IncrementalString.class));
        // entities
        entities.addAll(List.of(DGuild.class, DCharacter.class, DPlayer.class));
        entities.addAll(List.of(DBlacklist.class, DLoginQueue.class, DPlaySession.class));
        entities.addAll(List.of(DLevelupRun.class, DDungeonRun.class, DRaidRun.class));
        return entities;
    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setDdlGenerate(true);
        dbConfig.setDdlRun(CloverDatabaseConfig.get().getDDLRun());
        dbConfig.setDdlCreateOnly(CloverDatabaseConfig.get().getDDLCreateOnly());
        dbConfig.addAll(getEntities());
        return dbConfig;
    }

    @NotNull
    private static DataSourceConfig configureDataSource(CloverDatabaseConfig loadedConfig) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(loadedConfig.getUsername());
        dataSourceConfig.setPassword(loadedConfig.getPassword());
        dataSourceConfig.setUrl(loadedConfig.getUrl());
        return dataSourceConfig;
    }

    @Override
    public void onLoad() {
        if (!CloverDatabaseConfig.get().isConfigured()) {
            this.logger().fatal("Please configure " + this.databaseConfigFile.getAbsolutePath());
            System.exit(1);
        }
        DataSourceConfig dataSourceConfig = configureDataSource(CloverDatabaseConfig.get());
        DatabaseConfig dbConfig = configureDatabase(dataSourceConfig);
        DatabaseFactory.createWithContextClassLoader(dbConfig, CloverBot.class.getClassLoader());
        logger().info("Successfully created database");
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        Builder<CloverDatabaseConfig> databaseConfig = configJson(CloverDatabaseConfig.class, "Database.config", "Config");
        this.databaseConfigFile = this.getFile("Config", "Database.config.json");
        return List.of(databaseConfig);
    }

    @Override
    public String getName() {
        return "Database";
    }
}
