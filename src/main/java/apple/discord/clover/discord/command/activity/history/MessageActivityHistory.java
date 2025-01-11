package apple.discord.clover.discord.command.activity.history;

import apple.discord.clover.api.base.request.TimeResolution;
import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.base.MessageActivity;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.apache.commons.collections4.comparators.NullComparator;
import org.jetbrains.annotations.NotNull;

public class MessageActivityHistory extends MessageActivity {

    private static final Comparator<InactivePlayer> HISTORY_COMPARATOR = Comparator.comparing(
            (InactivePlayer p) -> p.getPlaytime(TimeResolution.MONTH, 3), new NullComparator<>())
        .thenComparing(InactivePlayer::getName, String.CASE_INSENSITIVE_ORDER);

    private Comparator<InactivePlayer> comparator = null;

    public MessageActivityHistory(GuiInactivity parent) {
        super(parent);
        registerSelectString("sortby", this::sortBy);
    }

    private void sortBy(StringSelectInteractionEvent event) {
        List<SelectOption> selectedOptions = event.getSelectedOptions();
        if (selectedOptions.isEmpty()) return;
        String sortBy = selectedOptions.getFirst().getValue();
        comparator = switch (sortBy) {
            case "2week" -> Comparator.<InactivePlayer, Duration>comparing(p -> p.getPlaytime(TimeResolution.WEEK, 2))
                .thenComparing(HISTORY_COMPARATOR);
            case "1month" -> Comparator.<InactivePlayer, Duration>comparing(p -> p.getPlaytime(TimeResolution.MONTH, 1))
                .thenComparing(HISTORY_COMPARATOR);
            case "3month" -> Comparator.<InactivePlayer, Duration>comparing(p -> p.getPlaytime(TimeResolution.MONTH, 3))
                .thenComparing(HISTORY_COMPARATOR);
            case "rank" -> Comparator.comparing(InactivePlayer::getGuildRank)
                .thenComparing(HISTORY_COMPARATOR);
            default -> HISTORY_COMPARATOR;
        };
        entryPage = 0;
        sort();
    }

    @Override
    protected LayoutComponent getSortByRow() {
        return ActionRow.of(
            StringSelectMenu.create("sortby")
                .setRequiredRange(1, 1)
                .setPlaceholder("Sort by")
                .addOption("3 Month Playtime", "3month", "Sort by playtime in the last 3 months")
                .addOption("1 Month Playtime", "1month", "Sort by playtime in the last month")
                .addOption("2 Week Playtime", "2week", "Sort by playtime in the last 2 weeks")
                .addOption("Rank", "rank", "Sort by the player's guild rank")
                .build()
        );
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
        if (comparator == null) comparator = HISTORY_COMPARATOR;
        return comparator;
    }
}
