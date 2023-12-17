package apple.discord.clover.discord.command.activity.base;

import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import apple.discord.clover.util.Pretty;
import discord.util.dcf.gui.scroll.DCFEntry;
import discord.util.dcf.gui.scroll.DCFScrollGui;
import java.time.Duration;
import java.util.Comparator;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public abstract class MessageActivity extends DCFScrollGui<GuiInactivity, InactivePlayer> {

    private static final String DIVIDER = "+%s+%s+%s+%s+\n".formatted(
        "-".repeat(29),
        "-".repeat(18),
        "-".repeat(18),
        "-".repeat(18));
    private boolean isReversed = false;

    public MessageActivity(GuiInactivity gui) {
        super(gui);
        this.setEntries(gui.getGuildMembers());
        this.sort();
        this.initButtons();
    }

    protected static String displayHours(Duration playtime) {
        if (playtime == null) return "??? Hours";
        long minutes = playtime.toMinutes();
        double hours = minutes / 60.0;
        return "%.1f Hours".formatted(hours);
    }

    private void initButtons() {
        registerButton(this.btnNext().getId(), event -> this.forward());
        registerButton(this.btnPrev().getId(), event -> this.back());
        registerButton(this.getReverseButton().getId(), event -> {
            this.isReversed = !this.isReversed;
            sort();
            this.entryPage = 0;
        });
        registerButton(this.getTopButton().getId(), interaction -> this.entryPage = 0);
    }

    private int getEntriesPerSection() {
        return 5;
    }

    @Override
    protected final int entriesPerPage() {
        return 15;
    }

    private ActionRow getNavigationRow() {
        return ActionRow.of(this.btnPrev(),
            this.btnNext(),
            this.getTopButton(),
            this.getReverseButton());
    }

    private Button getTopButton() {
        return Button.primary("top1", "To page 1");
    }

    private Button getReverseButton() {
        return Button.primary("reverse", "Reverse sort");
    }

    @Override
    public MessageCreateData makeMessage() {
        return new MessageCreateBuilder()
            .setContent(this.messageContent())
            .addComponents(this.getNavigationRow())
            .build();
    }

    private String messageContent() {
        StringBuilder content = new StringBuilder(header());
        for (DCFEntry<InactivePlayer> entry : this.getCurrentPageEntries()) {
            if (entry.indexInPage() % getEntriesPerSection() == 0)
                content.append(DIVIDER);
            content.append(this.asEntryString(entry));
            content.append("\n");
        }
        int limitMessage = Message.MAX_CONTENT_LENGTH - 5;
        return Pretty.limit(content.toString(), limitMessage) + "\n```";
    }

    private String asEntryString(DCFEntry<InactivePlayer> entry) {
        InactivePlayer player = entry.entry();
        return String.format("|%4d. %-23s|%17s |%17s |%17s |",
            entry.indexInAll() + 1,
            Pretty.limit(player.getName(), 23),
            Pretty.limit(value1(player), 17),
            Pretty.limit(value2(player), 17),
            Pretty.limit(value3(player), 17)
        );
    }

    private String header() {
        String guildName = Pretty.limit(this.parent.getGuildName(), 23 - " Members".length());
        return String.format("```ml\n|%5s %-23s| %-17s| %-17s| %-17s|\n", "", guildName + " Members", header1(), header2(), header3());
    }

    @NotNull
    protected String header1() {
        return "Rank";
    }

    protected String value1(InactivePlayer player) {
        return Pretty.uppercaseFirst(player.getGuildRank());
    }

    @NotNull
    protected abstract String header2();

    protected abstract String value2(InactivePlayer player);

    @NotNull
    protected abstract String header3();

    protected abstract String value3(InactivePlayer player);

    @Override
    protected final Comparator<? super InactivePlayer> entriesComparator() {
        Comparator<InactivePlayer> comparator = entriesComparatorDefault();
        return isReversed ? comparator.reversed() : comparator;
    }

    protected abstract Comparator<InactivePlayer> entriesComparatorDefault();
}
