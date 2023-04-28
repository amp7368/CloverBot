package apple.discord.clover.database.auth.permission;

import apple.discord.clover.database.auth.AuthQuery;
import java.util.UUID;

public enum DefaultAuthPermission {

    PLAYER_UUID("b6087df4-85e9-4eec-af80-6e09aac87523", "player.uuid"),
    MY_PLAYER_DATA("33ec37c7-8322-4a39-81a1-0991c58f2736", "player.myself"),
    ALL_PLAYER_DATA("f31757c9-5cdb-4782-8d2e-0fb49819fca4", "player.other"),
    GRANT_PERMISSION("40d98497-c416-484f-b062-88af3f47ea92", "auth.grant");

    private final UUID uuid;
    private final String name;
    private DAuthPermission permission;

    DefaultAuthPermission(String uuid, String name) {
        this.uuid = UUID.fromString(uuid);
        this.name = name;
    }


    public DAuthPermission get() {
        if (permission != null) return permission;
        return permission = AuthQuery.computePermission(this.uuid, this.name);
    }
}
