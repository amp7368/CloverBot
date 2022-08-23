package apple.discord.clover.discord.inactivity;

import apple.discord.acd.ACD;
import apple.discord.acd.gui.awd.main.AWDGui;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.guild.WynnGuild;
import apple.discord.clover.wynncraft.guild.WynnGuildHeader;
import apple.discord.clover.wynncraft.guild.WynnGuildMember;
import apple.discord.clover.wynncraft.player.WynnPlayer;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GuiInactivity extends AWDGui {
    private final List<WynnPlayer> members = new ArrayList<>();
    @NotNull
    private final WynnGuildHeader wynnGuildHeader;
    @Nullable
    private WynnGuild guild;

    public GuiInactivity(ACD acd, SlashCommandInteractionEvent event, @NotNull WynnGuildHeader wynnGuildHeader) {
        super(acd, event);
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
        WynnDatabase.addMember(player);
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
