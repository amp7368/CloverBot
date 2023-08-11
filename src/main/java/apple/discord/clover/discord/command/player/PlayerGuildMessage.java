package apple.discord.clover.discord.command.player;

import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.query.player.PlayerGuildChange;
import apple.discord.clover.discord.command.player.guild.PlayerGuildGui;
import apple.discord.clover.discord.util.CloverColor;
import apple.discord.clover.discord.util.DiscordPlayerUtils;
import apple.discord.clover.discord.util.SendMessage;
import discord.util.dcf.gui.scroll.DCFEntry;
import discord.util.dcf.gui.scroll.DCFScrollGui;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

public class PlayerGuildMessage extends DCFScrollGui<PlayerGuildGui, PlayerGuildChange> implements SendMessage {


    public PlayerGuildMessage(PlayerGuildGui parent) {
        super(parent);
        setEntries(parent.getGuildHistory());
    }

    @NotNull
    private MessageCreateData noChangesMessage() {
        EmbedBuilder embed = DiscordPlayerUtils.user(error(), parent.getPlayer());
        embed.setTitle("No Guild Changes")
            .setColor(CloverColor.GUILD)
            .setDescription("Note that guild changes before May of 2023 are not included");
        return MessageCreateData.fromEmbeds(embed.build());
    }

    @Override
    public MessageCreateData makeMessage() {
        List<DCFEntry<PlayerGuildChange>> currentPageEntries = getCurrentPageEntries();
        if (currentPageEntries.isEmpty()) {
            return noChangesMessage();
        }
        StringBuilder desc = new StringBuilder();

        boolean isFirstGuild = true;
        PlayerGuildChange lastChange = currentPageEntries.get(0).entry();

        if (lastChange.hasLastGuild()) {
            DGuild lastGuild = lastChange.getLastGuild();
            isFirstGuild = false;
            Instant time = lastChange.getRetrievedTime();
            desc.append(guildMsg(lastGuild, time, time, true, false));
            lastChange = null;
        }
        for (int i = 1, size = currentPageEntries.size(); i < size; i++) {
            PlayerGuildChange nextChange = currentPageEntries.get(i).entry();
            if (lastChange == null) {
                // put nextChange into lastChange
                lastChange = nextChange;
            } else {
                if (nextChange.hasLastGuild()) {
                    // left nextChange.lastGuild
                    DGuild guild = nextChange.getLastGuild();
                    Instant from = lastChange.getRetrievedTime();
                    Instant until = nextChange.getRetrievedTime();
                    desc.append(guildMsg(guild, from, until, isFirstGuild, false));
                    isFirstGuild = false;
                    lastChange = null;
                } else {
                    // joined nextChange.lastGUild
                    lastChange = nextChange;
                }
            }
        }
        if (lastChange != null) {
            DGuild guild = lastChange.getCurrentGuild();
            Instant time = lastChange.getRetrievedTime();
            desc.append(guildMsg(guild, time, time, isFirstGuild, true));
        }

        return buildCreate(embed()
            .setDescription(desc)
            .build());
    }

    private EmbedBuilder embed() {
        return DiscordPlayerUtils.user(parent.getPlayer())
            .setAuthor("Guild History")
            .setColor(CloverColor.GUILD);
    }


    private String guildMsg(DGuild guild, Instant from, Instant until, boolean isFirstGuild, boolean isLastGuild) {
        String name = guild.isActive() ? guild.getName() : "~~Deleted~~";
        String tag = guild.getTag();

        String time = "<t:%d:D>";
        String fromString = isFirstGuild ? "`long ago`" : time.formatted(from.getEpochSecond());
        String untilString = isLastGuild ? "`now`" : time.formatted(until.getEpochSecond());

        return "## In %s [%s]\n- from %s until %s\n".formatted(name, tag, fromString, untilString);
    }

    @Override
    protected Comparator<PlayerGuildChange> entriesComparator() {
        return Comparator.comparing(PlayerGuildChange::getRetrievedTime, Instant::compareTo).reversed();
    }

    @Override
    protected int entriesPerPage() {
        return 15;
    }
}
