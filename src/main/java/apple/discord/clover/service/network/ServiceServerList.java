package apple.discord.clover.service.network;

import apple.discord.clover.database.activity.partial.LoginStorage;
import apple.discord.clover.service.base.DaemonService;
import apple.discord.clover.wynncraft.WynnResponse;
import apple.discord.clover.wynncraft.WynncraftApi;
import apple.discord.clover.wynncraft.WynncraftModule;
import apple.discord.clover.wynncraft.network.ServerListResponse;
import discord.util.dcf.util.TimeMillis;
import java.util.concurrent.ForkJoinPool;
import okhttp3.CacheControl;
import okhttp3.Request.Builder;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class ServiceServerList extends DaemonService<ServerListResponse> {

    public static final long SERVER_LIST_OFFLINE_INTERVAL = TimeMillis.minToMillis(5);
    private static final Builder SERVER_LIST_REQUEST = new Builder().get()
        .url(WynncraftApi.SERVER_LIST)
        .cacheControl(CacheControl.FORCE_NETWORK);

    public ServiceServerList() {
        queueStart();
    }


    @Override
    protected void start() {
        ServiceServerListConfig.get().sleepIfLastQueryRecent(normalInterval());
        super.start();
    }

    @Override
    protected String name() {
        return "ServerListService";
    }

    @Override
    protected void updateLastQuery() {
        ServiceServerListConfig.get().updateLastQuery();
    }

    @Override
    protected boolean acceptResponse(WynnResponse<ServerListResponse> response) {
        if (response.data() == null || response.data().getPlayers() == null) return false;
        ForkJoinPool.commonPool().execute(
            () -> LoginStorage.queuePlayers(response.data().getPlayers(), response.retrieved()));
        return true;
    }

    @NotNull
    protected ServerListResponse deserialize(Response response) {
        return WynncraftModule.gson().fromJson(response.body().charStream(), ServerListResponse.class);
    }

    @Override
    protected Builder request() {
        return SERVER_LIST_REQUEST;
    }

    @Override
    protected long normalInterval() {
        return SERVER_LIST_OFFLINE_INTERVAL;
    }
}
