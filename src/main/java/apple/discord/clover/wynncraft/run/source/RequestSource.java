package apple.discord.clover.wynncraft.run.source;

import apple.discord.clover.wynncraft.run.WynnRequest;
import apple.discord.clover.wynncraft.run.WynncraftRatelimit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public abstract class RequestSource<T> {

    protected static final int MAX_LIVE_REQUESTS = 0;
    protected final List<T> next = new ArrayList<>();
    protected final Set<UUID> liveRequests = new HashSet<>();

    protected int requestCount = 0;

    public void submitRequest(WynnRequest<?> request) {
        liveRequests.add(request.getId());
        WynncraftRatelimit.queue(request);
    }

    public void completeRequest(WynnRequest<?> completed) {
        liveRequests.remove(completed.getId());
    }

    public abstract void ask();
}
