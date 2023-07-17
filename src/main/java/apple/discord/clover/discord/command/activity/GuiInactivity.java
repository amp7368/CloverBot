package apple.discord.clover.discord.command.activity;

import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.discord.command.activity.player.InactivePlayer;
import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class GuiInactivity extends DCFGui {

    private final List<InactivePlayer> members = new ArrayList<>();
    private final DGuild wynnGuildHeader;
    @Nullable
    private WynnGuild guild;

    public GuiInactivity(DCF dcf, GuiReplyFirstMessage firstMessage, DGuild wynnGuildHeader) {
        super(dcf, firstMessage);
        this.wynnGuildHeader = wynnGuildHeader;
    }

    public String getGuildName() {
        return wynnGuildHeader.getName();
    }

    public void addPlayer(InactivePlayer player) {
        synchronized (this) {
            this.members.add(player);
        }
    }

    public boolean isGuildMembersPresent() {
        synchronized (this) {
            int membersThere = this.members.size();
            int membersRequired = this.guild == null ? 0 : this.guild.members.length;
            return membersThere >= membersRequired;
        }
    }

    public void setGuild(WynnGuild wynnGuild) {
        synchronized (this) {
            this.guild = wynnGuild;
        }
    }

    public double getMembersProgress() {
        synchronized (this) {
            int membersThere = this.members.size();
            int membersRequired = this.guild == null ? 1 : this.guild.members.length;
            return membersThere / (double) membersRequired;
        }
    }

    public List<InactivePlayer> getGuildMembers() {
        return this.members;
    }

}
