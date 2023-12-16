package apple.discord.clover.service.player;

import apple.discord.clover.database.activity.partial.DLoginQueue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class LoginQueueUUID {

    private final DLoginQueue nextPlayer;
    private final List<UUID> uuids;

    public LoginQueueUUID(DLoginQueue nextPlayer, Collection<UUID> uuids) {
        this.nextPlayer = nextPlayer;
        this.uuids = new ArrayList<>(uuids);
    }

    public DLoginQueue getNextPlayer() {
        return nextPlayer;
    }

    public boolean isEmpty() {
        return uuids.isEmpty();
    }

    public UUID popUUID() {
        return uuids.remove(0);
    }

    public DLoginQueue login() {
        return this.nextPlayer;
    }
}
