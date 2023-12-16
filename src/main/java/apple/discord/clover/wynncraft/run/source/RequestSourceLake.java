package apple.discord.clover.wynncraft.run.source;

import apple.discord.clover.wynncraft.run.WynnRequest;
import java.util.Collection;

public abstract class RequestSourceLake<T> extends RequestSource<T> {


    public abstract Collection<T> fetchNext();

    public abstract WynnRequest<T> build();

    @Override
    public void ask() {
//        SourcesExecutor.submit(this::fetchNext);
    }
}
