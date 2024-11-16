package apple.discord.clover.service;

import apple.discord.clover.CloverConfig;
import apple.discord.clover.database.meta.status.ServiceActivityType;
import apple.lib.modules.configs.data.config.init.IAppleConfigInit;
import java.util.HashMap;
import java.util.Map;

public class ServiceModuleConfig implements IAppleConfigInit {

    protected boolean shouldEnable = true;
    protected Map<ServiceActivityType, ServiceActivityDownConfig> status = new HashMap<>();
    protected long pingTargetId = 253646208084475904L;
    protected transient String pingTarget;

    @Override
    public void onInitConfig() {
        status.computeIfAbsent(ServiceActivityType.DISCORD_BOT, ServiceActivityDownConfig::createNoPing);
        status.computeIfAbsent(ServiceActivityType.PROGRAM, ServiceActivityDownConfig::createNoPing);
        status.computeIfAbsent(ServiceActivityType.PLAY_SESSION, ServiceActivityDownConfig::createDefault);

        for (ServiceActivityType activityType : ServiceActivityType.values()) {
            status.computeIfAbsent(activityType, ServiceActivityDownConfig::createDefault);
        }
        CloverConfig.get().save();
        this.pingTarget = "<@%d>".formatted(pingTargetId);
    }

    public boolean shouldEnable() {
        return shouldEnable;
    }

    public ServiceActivityDownConfig getStatus(ServiceActivityType activity) {
        return status.get(activity);
    }

    public String getPingMention() {
        return this.pingTarget;
    }

    public long getPingTarget() {
        return this.pingTargetId;
    }
}
