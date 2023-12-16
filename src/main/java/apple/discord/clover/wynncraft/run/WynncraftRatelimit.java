package apple.discord.clover.wynncraft.run;

import apple.discord.clover.wynncraft.overview.guild.response.RepeatThrottle;
import apple.discord.clover.wynncraft.run.source.RequestSource;
import discord.util.dcf.util.TimeMillis;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class WynncraftRatelimit {

    private static final int REQUESTS_PER_INTERVAL = 275;
    private static final long INTERVAL = TimeMillis.minToMillis(5);
    private static final int QUEUE_LENGTH = 10;
    private static final int CONCURRENCY = 3;

    private static final RepeatThrottle rateLimit = new RepeatThrottle(1000);

    private static final List<WynnRequest<?>> QUEUE = new ArrayList<>();
    private static final List<WynnRequest<?>> REQUESTS = new ArrayList<>();

    private static final List<RequestSource<?>> SOURCES = new ArrayList<>();

    private static final List<CompletedWynnRequest> recentRequests = new ArrayList<>();

    public static void queue(WynnRequest<?> request) {
        synchronized (REQUESTS) {
            REQUESTS.add(request);
            verifyStarted();
        }
    }

    private static void verifyStarted() {
        
    }

    public void refreshQueue() {
        // if queue is good, do nothing
        synchronized (QUEUE) {
            if (QUEUE.size() >= QUEUE_LENGTH) return;
        }
        if (checkRequestCache()) return;
        List<WynnRequest<?>> toAdd = chooseNextRequests();
        addNextRequests(toAdd);
    }

    /**
     * if REQUESTS are empty, ask sources for more
     *
     * @return true if REQUESTS was empty
     */
    private boolean checkRequestCache() {
        // if no requests in cache, ask for more
        boolean askQueue;
        synchronized (REQUESTS) {
            askQueue = REQUESTS.isEmpty();
        }
        if (askQueue) {
            synchronized (SOURCES) {
                SOURCES.forEach(RequestSource::ask);
            }
            return true;
        }
        return false;
    }

    @NotNull
    private List<WynnRequest<?>> chooseNextRequests() {
        int toAddSize;
        synchronized (QUEUE) {
            toAddSize = QUEUE_LENGTH - QUEUE.size();
        }
        List<WynnRequest<?>> toAdd = new ArrayList<>(toAddSize);
        synchronized (REQUESTS) {
            REQUESTS.sort(WynnRequest.WYNN_REQUEST_COMPARATOR);
            for (int i = toAddSize; i < QUEUE_LENGTH; i++) {
                toAdd.add(REQUESTS.remove(0));
            }
        }
        return toAdd;
    }

    private void addNextRequests(List<WynnRequest<?>> toAdd) {
        synchronized (QUEUE) {
            while (!toAdd.isEmpty()) {
                if (QUEUE.size() >= QUEUE_LENGTH) break;
                QUEUE.add(toAdd.remove(0));
            }
        }
        if (toAdd.isEmpty()) return;
        synchronized (REQUESTS) {
            REQUESTS.addAll(toAdd);
        }
    }


}
