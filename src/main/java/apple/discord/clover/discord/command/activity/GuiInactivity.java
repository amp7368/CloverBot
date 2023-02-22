package apple.discord.clover.discord.command.activity;

import apple.discord.clover.wynncraft.stats.guild.WynnGuild;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildHeader;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiInactivity extends DCFGui {

    private final List<WynnPlayer> members = new ArrayList<>();
    @NotNull
    private final WynnGuildHeader wynnGuildHeader;
    @Nullable
    private WynnGuild guild;

    public GuiInactivity(DCF dcf, GuiReplyFirstMessage firstMessage, @NotNull WynnGuildHeader wynnGuildHeader) {
        super(dcf, firstMessage);
        this.wynnGuildHeader = wynnGuildHeader;
    }

    public String getGuildName() {
        return wynnGuildHeader.name;
    }

    public void addPlayer(WynnGuildMember guildMember, WynnPlayer player) {
        synchronized (this) {
            this.members.add(player);
            player.addGuildMemberInfo(guildMember);
        }
    }

    public boolean isGuildMembersPresent() {
        synchronized (this) {
            int membersThere = this.members.size();
            int membersRequired = this.guild == null ? 1 : this.guild.members.length;
            System.out.println(membersThere + " " + membersRequired);
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

    public List<WynnPlayer> getGuildMembers() {
        return this.members;
    }
}
