package apple.discord.clover.database;

import apple.discord.clover.CloverBot;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.data.config.AppleConfig.Builder;
import apple.lib.modules.configs.factory.AppleConfigLike;
import io.ebean.DatabaseFactory;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.dbmigration.DbMigration;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CloverDatabase extends AppleModule {

    private File databaseConfigFile;

    public static void main(String[] args) throws IOException {
        // doesn't work
        DbMigration migration = DbMigration.create();
        migration.setPlatform(Platform.MYSQL);
        migration.generateMigration();
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

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setDdlGenerate(true);
        dbConfig.setDdlRun(CloverDatabaseConfig.get().getDDLRun());

        return dbConfig;
    }

    @NotNull
    private static DataSourceConfig configureDataSource(CloverDatabaseConfig loadedConfig) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUsername(loadedConfig.getUsername());
        dataSourceConfig.setPassword(loadedConfig.getPassword());
        dataSourceConfig.setUrl(loadedConfig.getUrl());
        dataSourceConfig.setAutoCommit(true);
        return dataSourceConfig;
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
