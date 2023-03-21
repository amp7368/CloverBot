package apple.discord.clover.database;

public class CloverDatabaseConfig {

    private static CloverDatabaseConfig instance;
    public boolean isConfigured = false;

    public String username = "${username}";
    public String password = "${password}";
    public String host = "${host}";
    public String port = "${port}";
    public String database = "add";
    public boolean RUN_MIGRATION = false;
    public boolean CREATE_DATABASE = true;

    public CloverDatabaseConfig() {
        instance = this;
    }

    public static CloverDatabaseConfig get() {
        return instance;
    }

    public String getUrl() {
        return "jdbc:postgresql://" + this.host + ":" + this.port + "/" + this.database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isConfigured() {
        return this.isConfigured;
    }

    public boolean getDDLMigration() {
        return this.RUN_MIGRATION;
    }

    public boolean getDDLCreateDatabase() {
        return this.CREATE_DATABASE;
    }
}
