package apple.discord.clover.discord.command.activity.last_join;

import apple.discord.clover.api.base.request.TimeResolution;
import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.base.MessageActivity;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import java.time.Duration;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;

public class MessageActivityLastJoin extends MessageActivity {

    private static final Comparator<InactivePlayer> LAST_JOIN_COMPARATOR = (p1, p2) -> {
        long timeCompared = p1.getLastJoin().compareTo(p2.getLastJoin());
        if (timeCompared > 0) return 1;
        else if (timeCompared == 0) {
            return p1.getName().compareTo(p2.getName());
        }
        return -1;
    };

    public MessageActivityLastJoin(GuiInactivity gui) {
        super(gui);
    }

    @NotNull
    @Override
    protected String header2() {
        return "Time Inactive";
    }

    @Override
    protected String value2(InactivePlayer player) {
        Duration inactive = player.getInactiveDuration();
        long days = inactive == null ? -1 : inactive.toDays();
        if (days < 0) return "Error";
        else if (days == 1) return days + " day";
        else return days + " days";
    }

    @NotNull
    @Override
    protected String header3() {
        return "Week Playtime";
    }

    @Override
    protected String value3(InactivePlayer player) {
        Duration playtime = player.getPlaytime(TimeResolution.WEEK, 1);
        if (playtime == null) return "??? Hours";
        long minutes = playtime.toMinutes();
        double hours = minutes / 60.0;
        return "%.1f Hours".formatted(hours);
    }


    @Override
    protected Comparator<InactivePlayer> entriesComparatorDefault() {
        return LAST_JOIN_COMPARATOR;
    }

}
