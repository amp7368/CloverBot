package apple.discord.clover.discord.command.activity.history;

import apple.discord.clover.api.base.request.TimeResolution;
import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.base.MessageActivity;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import java.time.Duration;
import java.util.Comparator;
import org.apache.commons.collections4.comparators.NullComparator;
import org.jetbrains.annotations.NotNull;

public class MessageActivityHistory extends MessageActivity {

    private static final Comparator<InactivePlayer> HISTORY_COMPARATOR = Comparator.comparing(
            (InactivePlayer p) -> p.getPlaytime(TimeResolution.MONTH, 3), new NullComparator<>())
        .thenComparing(InactivePlayer::getName, String.CASE_INSENSITIVE_ORDER);

    public MessageActivityHistory(GuiInactivity parent) {
        super(parent);
    }

    @Override
    protected @NotNull String header1() {
        return "3 Month Playtime";
    }

    @Override
    protected String value1(InactivePlayer player) {
        Duration playtime = player.getPlaytime(TimeResolution.MONTH, 3);
        return displayHours(playtime);
    }

    @Override
    protected @NotNull String header2() {
        return "Month Playtime";
    }


    @Override
    protected String value2(InactivePlayer player) {
        Duration playtime = player.getPlaytime(TimeResolution.MONTH, 1);
        return displayHours(playtime);
    }

    @Override
    protected @NotNull String header3() {
        return "2 Week Playtime";
    }

    @Override
    protected String value3(InactivePlayer player) {
        Duration playtime = player.getPlaytime(TimeResolution.WEEK, 2);
        return displayHours(playtime);
    }

    @Override
    protected Comparator<InactivePlayer> entriesComparatorDefault() {
        return HISTORY_COMPARATOR;
    }
}
