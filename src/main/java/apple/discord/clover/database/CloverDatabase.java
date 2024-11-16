package apple.discord.clover.database;

import apple.discord.clover.CloverBot;
import apple.discord.clover.api.base.BaseEntity;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.blacklist.DBlacklist;
import apple.discord.clover.database.activity.partial.DLoginQueue;
import apple.discord.clover.database.activity.run.DDungeonRun;
import apple.discord.clover.database.activity.run.DLevelupRun;
import apple.discord.clover.database.activity.run.DRaidRun;
import apple.discord.clover.database.activity.run.DSessionRunBase;
import apple.discord.clover.database.auth.authentication.DAuthentication;
import apple.discord.clover.database.auth.authentication.token.DAuthToken;
import apple.discord.clover.database.auth.identity.DAuthIdentity;
import apple.discord.clover.database.auth.identity.DIdentityRoleBridge;
import apple.discord.clover.database.auth.identity.DUserBasicCredentials;
import apple.discord.clover.database.auth.permission.DAuthPermission;
import apple.discord.clover.database.auth.role.DAuthRole;
import apple.discord.clover.database.auth.role.DRolePermissionBridge;
import apple.discord.clover.database.benchmark.PasswordBenchmark;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.log.DCommandLog;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.primitive.IncrementalBigInt;
import apple.discord.clover.database.primitive.IncrementalFloat;
import apple.discord.clover.database.primitive.IncrementalInt;
import apple.discord.clover.database.primitive.IncrementalString;
import apple.discord.clover.database.status.DServiceStatus;
import apple.discord.clover.database.status.notification.DServiceStatusNotification;
import apple.discord.clover.database.user.DUser;
import apple.discord.clover.database.user.DUserDiscord;
import apple.discord.clover.database.user.DUserMinecraft;
import apple.lib.modules.AppleModule;
import apple.lib.modules.configs.data.config.AppleConfig.Builder;
import apple.lib.modules.configs.factory.AppleConfigLike;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.config.dbplatform.DatabasePlatform;
import io.ebean.datasource.DataSourceConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class CloverDatabase extends AppleModule {

    public static DatabasePlatform DATABASE_PLATFORM;
    private static CloverDatabase instance;
    private File databaseConfigFile;

    public CloverDatabase() {
        instance = this;
    }

    public static CloverDatabase get() {
        return instance;
    }

    private static List<Class<?>> getEntities() {
        List<Class<?>> entities = new ArrayList<>();
        // superclass
        entities.add(BaseEntity.class);
        entities.add(DSessionRunBase.class);
        // embedded
        entities.addAll(List.of(IncrementalInt.class, IncrementalBigInt.class, IncrementalFloat.class, IncrementalString.class));
        // wynn entities
        entities.add(DVersion.class);
        entities.addAll(List.of(DGuild.class, DCharacter.class, DPlayer.class));
        entities.addAll(List.of(DBlacklist.class, DLoginQueue.class, DPlaySession.class));
        entities.addAll(List.of(DLevelupRun.class, DDungeonRun.class, DRaidRun.class));

        // user entities
        entities.addAll(List.of(DUser.class, DUserMinecraft.class, DUserDiscord.class));

        // web entities
        entities.addAll(List.of(DIdentityRoleBridge.class, DRolePermissionBridge.class, DAuthPermission.class, DAuthRole.class));
        entities.addAll(List.of(DAuthIdentity.class, DUserBasicCredentials.class));
        entities.addAll(List.of(DAuthentication.class, DAuthToken.class));

        // system entities
        entities.add(DCommandLog.class);
        entities.addAll(List.of(DServiceStatus.class, DServiceStatusNotification.class));
        return entities;
    }

    @NotNull
    private static DatabaseConfig configureDatabase(DataSourceConfig dataSourceConfig) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setDataSourceConfig(dataSourceConfig);
        dbConfig.setDdlGenerate(true);
        dbConfig.setDdlRun(CloverDatabaseConfig.get().getDDLCreateDatabase());
        dbConfig.setRunMigration(CloverDatabaseConfig.get().getDDLMigration());
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
        if (CloverDatabaseConfig.get().isRunBenchmark()) {
            PasswordBenchmark.benchmark(logger());
            System.exit(1);
        }
        if (!CloverDatabaseConfig.get().isConfigured()) {
            this.logger().fatal("Please configure " + this.databaseConfigFile.getAbsolutePath());
            System.exit(1);
        }
        DataSourceConfig dataSourceConfig = configureDataSource(CloverDatabaseConfig.get());
        DatabaseConfig dbConfig = configureDatabase(dataSourceConfig);
        DatabaseFactory.createWithContextClassLoader(dbConfig, CloverBot.class.getClassLoader());
        DATABASE_PLATFORM = dbConfig.getDatabasePlatform();
        logger().info("Successfully created database");
    }

    @Override
    public List<AppleConfigLike> getConfigs() {
        Builder<CloverDatabaseConfig> databaseConfig = configJson(CloverDatabaseConfig.class, "Database.config");
        this.databaseConfigFile = this.getFile("Database.config.json");
        return List.of(databaseConfig);
    }

    @Override
    public String getName() {
        return "Database";
    }
}
