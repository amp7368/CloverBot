package apple.discord.clover.discord.command.player.guild;

import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.query.player.PlayerGuildChange;
import apple.discord.clover.discord.util.CloverColor;
import apple.discord.clover.discord.util.DiscordPlayerUtils;
import apple.discord.clover.discord.util.SendMessage;
import discord.util.dcf.gui.scroll.DCFEntry;
import discord.util.dcf.gui.scroll.DCFScrollGui;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class PlayerGuildMessage extends DCFScrollGui<PlayerGuildGui, PlayerGuildDuration> implements SendMessage {


    private boolean isReversed = false;

    public PlayerGuildMessage(PlayerGuildGui parent) {
        super(parent);
        initButtons();
        setEntries(createEntries());
        sort();
    }

    private void initButtons() {
        registerButton(btnReverse().getId(), e -> {
            this.isReversed = !this.isReversed;
            this.sort();
        });
    }

    private List<PlayerGuildDuration> createEntries() {
        List<PlayerGuildChange> guildHistory = getParent().getGuildHistory();
        if (guildHistory.isEmpty()) return List.of();

        List<PlayerGuildDuration> entries = new ArrayList<>();

        boolean isFirstGuild = true;
        PlayerGuildChange lastChange = guildHistory.get(0);

        if (lastChange.hasLastGuild()) {
            // left lastChange.lastGuild
            DGuild lastGuild = lastChange.getLastGuild();
            Instant time = lastChange.getRetrievedTime();
            entries.add(new PlayerGuildDuration(lastGuild, time, time, true, false));
            isFirstGuild = false;
            lastChange = null;
        }
        for (int i = 1, size = guildHistory.size(); i < size; i++) {
            PlayerGuildChange nextChange = guildHistory.get(i);
            if (lastChange != null && nextChange.hasLastGuild()) {
                // left nextChange.lastGuild
                DGuild guild = nextChange.getLastGuild();
                Instant from = lastChange.getRetrievedTime();
                Instant until = nextChange.getRetrievedTime();
                entries.add(new PlayerGuildDuration(guild, from, until, isFirstGuild, false));
                isFirstGuild = false;
                lastChange = null;
            } else {
                // joined nextChange.lastGuild
                lastChange = nextChange;
            }
        }
        if (lastChange != null) {
            DGuild guild = lastChange.getCurrentGuild();
            Instant time = lastChange.getRetrievedTime();
            entries.add(new PlayerGuildDuration(guild, time, time, isFirstGuild, true));
        }
        return entries;
    }


    @NotNull
    private String footer() {
        return "Note that guild changes before April of 2023 are not included";
    }

    @Override
    public MessageCreateData makeMessage() {
        List<DCFEntry<PlayerGuildDuration>> currentPageEntries = getCurrentPageEntries();
        if (currentPageEntries.isEmpty())
            return noChangesMessage();

        String desc = currentPageEntries.stream()
            .map(DCFEntry::entry)
            .map(PlayerGuildDuration::guildMsg)
            .collect(Collectors.joining("\n"));

        return buildCreate(embed(desc).build(), getNavigationRow());
    }

    @NotNull
    private MessageCreateData noChangesMessage() {

        return buildCreate(embed("### No Guild Changes").build());
    }

    private LayoutComponent getNavigationRow() {
        return ActionRow.of(btnLast(), btnNext(), btnReverse());
    }

    private Button btnReverse() {
        return Button.primary("reverse", "Reverse sort");
    }

    private EmbedBuilder embed(String desc) {
        return DiscordPlayerUtils.user(parent.getPlayer())
            .setAuthor("Guild History")
            .setColor(CloverColor.PLAYER)
            .setDescription(desc)
            .setFooter(footer());
    }


    @Override
    protected Comparator<PlayerGuildDuration> entriesComparator() {
        Comparator<PlayerGuildDuration> comparator = Comparator.comparing(PlayerGuildDuration::until, Instant::compareTo);
        return this.isReversed ? comparator.reversed() : comparator;
    }

    @Override
    protected int entriesPerPage() {
        return 30;
    }
}
