package apple.discord.clover.discord.command.activity.pages;

import apple.discord.clover.discord.command.activity.GuiInactivity;
import apple.discord.clover.discord.command.activity.player.InactivePlayer;
import apple.discord.clover.util.Pretty;
import discord.util.dcf.gui.scroll.DCFEntry;
import discord.util.dcf.gui.scroll.DCFScrollGui;
import java.time.Duration;
import java.util.Comparator;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class MessageInactivity extends DCFScrollGui<GuiInactivity, InactivePlayer> {

    private static final Comparator<InactivePlayer> MEMBERS_COMPARATOR = (p1, p2) -> {
        long timeCompared = p1.getLastJoin().compareTo(p2.getLastJoin());
        if (timeCompared > 0) return 1;
        else if (timeCompared == 0) {
            return p1.getName().compareTo(p2.getName());
        }
        return -1;
    };
    private static final String DIVIDER = "+" + "-".repeat(36) + "+" + "-".repeat(26) + "+" + "-".repeat(26) + "+\n";

    private boolean isReversed = false;

    public MessageInactivity(GuiInactivity gui) {
        super(gui);
        this.addEntries(gui.getGuildMembers());
        this.sort();
    }

    private int getEntriesPerSection() {
        return 5;
    }


    @Override
    public MessageCreateData makeMessage() {
        return new MessageCreateBuilder()
            .setContent(this.messageContent())
            .addComponents(this.getNavigationRow())
            .build();
    }

    private String messageContent() {
        StringBuilder content = new StringBuilder(
            String.format("```ml\n|%5s %-30s| %-25s| %-25s|\n", "", this.parent.getGuildName() + " Members", "Rank", "Time Inactive"));
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
        Duration inactive = player.getInactiveDuration();
        long days = inactive == null ? -1 : inactive.toDays();
        String daysInactive;
        if (days < 0) daysInactive = "Error";
        else if (days == 1) daysInactive = days + " day";
        else daysInactive = days + " days";
        return String.format("|%4d. %-30s| %-25s| %-25s|",
            entry.indexInAll() + 1,
            Pretty.limit(player.getName(), 30),
            Pretty.uppercaseFirst(player.getGuildRank()),
            daysInactive);
    }

    @Override
    protected Comparator<InactivePlayer> entriesComparator() {
        return isReversed ? MEMBERS_COMPARATOR.reversed() : MEMBERS_COMPARATOR;
    }

    @Override
    protected int entriesPerPage() {
        return 15;
    }


    private void resetPage() {
        this.entryPage = 0;
    }

    public void onReverse(ButtonInteraction interaction) {
        this.isReversed = !this.isReversed;
        sort();
        resetPage();
    }

    private ActionRow getNavigationRow() {
        Button backButton = this.getBackButton();
        Button forwardButton = this.getForwardButton();
        if (this.getMaxPage() == this.entryPage) forwardButton = forwardButton.asDisabled();
        if (0 == this.entryPage) backButton = backButton.asDisabled();
        return ActionRow.of(backButton, forwardButton, this.getTopButton(), this.getReverseButton());
    }

    private Button getForwardButton() {
        registerButton(this.btnNext().getId(), event -> this.forward());
        return this.btnNext();
    }

    private Button getBackButton() {
        registerButton(this.btnPrev().getId(), event -> this.btnPrev());
        return this.btnPrev();
    }

    private Button getTopButton() {
        registerButton("top1", interaction -> resetPage());
        return Button.primary("top1", "To page 1");
    }

    private Button getReverseButton() {
        registerButton("reverse", this::onReverse);
        return Button.primary("reverse", "Reverse sort");
    }
}
