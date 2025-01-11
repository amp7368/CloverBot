package apple.discord.clover.discord.command.activity.last_join;

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
import org.jetbrains.annotations.NotNull;

public class MessageActivityLastJoin extends MessageActivity {

    private static final Comparator<InactivePlayer> LAST_JOIN_COMPARATOR = (p1, p2) -> {
        long timeCompared = p1.getLastJoin().compareTo(p2.getLastJoin());
        if (timeCompared > 0) return 1;
        else if (timeCompared == 0) {
            return p1.getName().compareToIgnoreCase(p2.getName());
        }
        return -1;
    };
    private Comparator<InactivePlayer> comparator;

    public MessageActivityLastJoin(GuiInactivity gui) {
        super(gui);
        registerSelectString("sortby", this::sortBy);
    }

    private void sortBy(StringSelectInteractionEvent event) {
        List<SelectOption> selectedOptions = event.getSelectedOptions();
        if (selectedOptions.isEmpty()) return;
        String sortBy = selectedOptions.getFirst().getValue();
        comparator = switch (sortBy) {
            case "1week" -> Comparator.<InactivePlayer, Duration>comparing(p -> p.getPlaytime(TimeResolution.WEEK, 1))
                .thenComparing(LAST_JOIN_COMPARATOR);
            case "rank" -> Comparator.comparing(InactivePlayer::getGuildRank)
                .thenComparing(LAST_JOIN_COMPARATOR);
            default -> LAST_JOIN_COMPARATOR;
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
                .addOption("Rank", "rank", "Sort by the player's guild rank")
                .addOption("Time Inactive", "lastjoin", "Sort by player's last join")
                .addOption("Week Playtime", "1week", "Sort by playtime in the last week")
                .build()
        );
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
        return displayHours(playtime);
    }

    @Override
    protected Comparator<InactivePlayer> entriesComparatorDefault() {
        if (comparator == null) comparator = LAST_JOIN_COMPARATOR;
        return comparator;
    }

}
