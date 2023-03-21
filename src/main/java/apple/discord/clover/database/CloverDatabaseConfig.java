package apple.discord.clover.database;

public class CloverDatabaseConfig {

    private static CloverDatabaseConfig instance;
    public boolean isConfigured = false;

    public String username = "${username}";
    public String password = "${password}";
    public String host = "${host}";
    public String port = "${port}";
    public String database = "add";
    public boolean DROP_THE_DATABASE_AND_RECREATE = false;
    public boolean createOnly = true;

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

    public boolean getDDLRun() {
        return this.DROP_THE_DATABASE_AND_RECREATE;
    }

    public boolean getDDLCreateOnly() {
        return this.createOnly;
    }
}
