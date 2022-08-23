package apple.discord.clover.discord.inactivity;

import apple.discord.acd.gui.awd.page.scrollable.AWDEntry;
import apple.discord.acd.gui.awd.page.scrollable.AWDPageGuiScrollable;
import apple.discord.clover.util.Pretty;
import apple.discord.clover.wynncraft.player.WynnPlayer;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

import java.util.Comparator;

public class MessageInactivity extends AWDPageGuiScrollable<GuiInactivity, WynnPlayer> {
    private static final Comparator<WynnPlayer> MEMBERS_COMPARATOR = (p1, p2) -> {
        long time = (p1.meta.lastJoin.getTime() - p2.meta.lastJoin.getTime());
        if (time > 0) return 1;
        else if (time == 0) {
            return p1.username.compareTo(p2.username);
        }
        return -1;
    };
    private static final String DIVIDER = "+" + "-".repeat(36) + "+" + "-".repeat(26) + "+" + "-".repeat(26) + "+\n";

    private boolean isReversed = false;

    public MessageInactivity(GuiInactivity gui) {
        super(gui);
        this.addAllEntries(gui.getGuildMembers());
        this.sort();
    }

    private int getEntriesPerSection() {
        return 5;
    }


    @Override
    public Message makeMessage() {
        return this.messageBuilder()
                .setContent(this.messageContent())
                .setActionRows(this.getNavigationRow())
                .build();
    }

    private String messageContent() {
        StringBuilder content = new StringBuilder(String.format("```ml\n|%5s %-30s| %-25s| %-25s|\n", "", this.parent.getGuildName() + " Members", "Rank", "Time Inactive"));
        for (AWDEntry<WynnPlayer> entry : this.getCurrentPageEntries()) {
            if (entry.indexInPage() % getEntriesPerSection() == 0)
                content.append(DIVIDER);
            content.append(this.asEntryString(entry));
            content.append("\n");
        }
        int limitMessage = Message.MAX_CONTENT_LENGTH - 5;
        return Pretty.limit(content.toString(), limitMessage) + "\n```";
    }

    private String asEntryString(AWDEntry<WynnPlayer> entry) {
        WynnPlayer player = entry.entry();
        long days = player.getInactiveDays();
        String daysInactive;
        if (days < 0) daysInactive = "Error";
        else if (days == 1) daysInactive = days + " day";
        else daysInactive = days + " days";
        return String.format("|%4d. %-30s| %-25s| %-25s|",
                entry.indexInAll() + 1,
                Pretty.limit(player.username, 30),
                Pretty.uppercaseFirst(player.guildMember.rank),
                daysInactive);
    }

    @Override
    protected Comparator<WynnPlayer> entriesComparator() {
        return isReversed ? MEMBERS_COMPARATOR.reversed() : MEMBERS_COMPARATOR;
    }

    @Override
    protected int entriesPerPage() {
        return 15;
    }


    private void resetPage() {
        this.pageInEntries = 0;
    }

    public void onReverse(ButtonInteraction interaction) {
        this.isReversed = !this.isReversed;
        sort();
        resetPage();
    }

    private ActionRow getNavigationRow() {
        Button backButton = this.getBackButton();
        Button forwardButton = this.getForwardButton();
        if (this.getMaxPage() == this.pageInEntries) forwardButton = forwardButton.asDisabled();
        if (0 == this.pageInEntries) backButton = backButton.asDisabled();
        return ActionRow.of(backButton, forwardButton, this.getTopButton(), this.getReverseButton());
    }

    private Button getForwardButton() {
        addManualSimpleButton(event -> this.forward(), this.btnForwardId());
        return this.btnForward();
    }

    private Button getBackButton() {
        addManualSimpleButton(event -> this.back(), this.btnBackId());
        return this.btnBack();
    }

    private Button getTopButton() {
        addManualSimpleButton(interaction -> resetPage(), "top1");
        return Button.primary("top1", "To page 1");
    }

    private Button getReverseButton() {
        addManualSimpleButton(this::onReverse, "reverse");
        return Button.primary("reverse", "Reverse sort");
    }
}
