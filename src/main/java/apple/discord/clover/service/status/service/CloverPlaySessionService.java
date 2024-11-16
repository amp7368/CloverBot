package apple.discord.clover.service.status.service;

import apple.discord.clover.CloverConfig;
import apple.discord.clover.database.meta.status.ServiceActivityType;
import apple.discord.clover.database.meta.status.ServiceStatusApi;
import apple.discord.clover.database.player.DPlaySession;
import apple.discord.clover.database.player.query.QDPlaySession;
import apple.discord.clover.service.status.CloverStatusService;
import java.time.Duration;
import java.time.Instant;

public class CloverPlaySessionService extends CloverStatusService {


    @Override
    public void run() {
        DPlaySession lastPlaySession = new QDPlaySession()
            .orderBy().retrievedTime.desc()
            .setMaxRows(1)
            .findOne();
        if (lastPlaySession == null) return;
        Instant lastOnline = lastPlaySession.getRetrievedAt();
        Duration timeInactive = Duration.between(lastOnline, Instant.now());
        boolean isDown = CloverConfig.getService().getStatus(getActivity()).isConsideredDown(timeInactive);
        if (isDown) {
            ServiceStatusApi.extendOnline(getActivity(), lastOnline);
            markOffline(Instant.now(), false);
        } else {
            markOnline(lastOnline, false);
        }
    }

    @Override
    protected ServiceActivityType getActivity() {
        return ServiceActivityType.PLAY_SESSION;
    }
}
