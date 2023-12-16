package apple.discord.clover.discord.command.activity;

import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.player.PlayerStorage;
import apple.discord.clover.discord.command.activity.base.player.InactiveDPlayer;
import apple.discord.clover.discord.command.activity.base.player.InactiveNotFoundPlayer;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import apple.discord.clover.discord.command.activity.base.player.InactiveWynnPlayer;
import apple.discord.clover.discord.util.EditOnTimer;
import apple.discord.clover.util.Pretty;
import apple.discord.clover.wynncraft.run.WynncraftOldRatelimit;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import discord.util.dcf.util.IMessageBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class MessageInactivityProgress extends DCFGuiPage<GuiInactivity> implements IMessageBuilder {

    private final EditOnTimer editMessage;
    private final Runnable onFinished;

    public MessageInactivityProgress(GuiInactivity parent, Runnable onFinished) {
        super(parent);
        this.onFinished = onFinished;
        this.editMessage = new EditOnTimer(this::editMessage, 1500);
        WynncraftOldRatelimit.queueGuild(TaskPriorityCommon.HIGH, parent.getGuildName(), wynnGuild -> {
            this.parent.setGuild(wynnGuild);
            this.editMessage.tryRun();
            if (wynnGuild == null) {
                checkIsFinished();
                return;
            }
            for (WynnGuildMember guildMember : wynnGuild.getMembers()) {
                if (guildMember == null) {
                    addPlayer(new InactiveNotFoundPlayer(null));
                    continue;
                }
                DPlayer dPlayer = PlayerStorage.findPlayer(guildMember.uuid);
                if (dPlayer == null) {
                    WynncraftOldRatelimit.queuePlayer(TaskPriorityCommon.HIGHEST, guildMember.uuid,
                        player -> this.addPlayer(new InactiveWynnPlayer(guildMember, player)));
                } else {
                    addPlayer(new InactiveDPlayer(guildMember, dPlayer));
                }
            }
        });
    }

    private void addPlayer(InactivePlayer player) {
        this.parent.addPlayer(player);
        checkIsFinished();
    }

    private void checkIsFinished() {
        if (this.parent.isGuildMembersPresent()) {
            this.onFinished.run();
            this.editMessage.tryRun();
        } else {
            this.editMessage.tryRun();
        }
    }

    @Override
    public MessageCreateData makeMessage() {
        double progress = this.parent.getMembersProgress();
        return buildCreate("Loading...\n" + Pretty.getProgress(progress));
    }
}
