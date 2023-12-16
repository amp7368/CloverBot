package apple.discord.clover.wynncraft.run;

import apple.discord.clover.wynncraft.run.source.RequestSource;
import java.util.Comparator;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WynnRequest<T> {

    public static Comparator<WynnRequest<?>> WYNN_REQUEST_COMPARATOR = (o1, o2) -> {
        if (o1.isImmediate != o2.isImmediate)
            return o1.isImmediate ? 1 : -1;
        return o2.type.priority() - o1.type.priority();
    };
    
    private final UUID id = UUID.randomUUID();
    private WynncraftRateType type;
    private Supplier<T> runnable;
    private Consumer<T> fn;
    private RequestSource<T> originator;
    private boolean isImmediate;

    public UUID getId() {
        return this.id;
    }
}
