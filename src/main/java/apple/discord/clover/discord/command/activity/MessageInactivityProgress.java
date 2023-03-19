package apple.discord.clover.discord.command.activity;

import apple.discord.clover.util.Pretty;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynncraftRatelimit;
import apple.discord.clover.wynncraft.stats.guild.WynnGuildMember;
import apple.discord.clover.wynncraft.stats.player.WynnPlayer;
import apple.utilities.threading.service.priority.TaskPriorityCommon;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import discord.util.dcf.util.IMessageBuilder;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.Nullable;

public class MessageInactivityProgress extends DCFGuiPage<GuiInactivity> implements IMessageBuilder {

    private final AtomicLong lastUpdated = new AtomicLong(System.currentTimeMillis());
    private final Runnable onFinished;

    public MessageInactivityProgress(GuiInactivity parent, Runnable onFinished) {
        super(parent);
        this.onFinished = onFinished;
        WynncraftRatelimit.queueGuild(TaskPriorityCommon.HIGH, parent.getGuildName(), wynnGuild -> {
            this.parent.setGuild(wynnGuild);
            editMessageOnTimer();
            if (wynnGuild == null) {
                return;
            }
            for (WynnGuildMember guildMember : List.of(wynnGuild.members)) {
                if (guildMember == null)
                    continue;
                @Nullable WynnPlayer player = WynnDatabase.get().getPlayer(guildMember.uuid);
                if (player == null) {
                    WynncraftRatelimit.queuePlayer(TaskPriorityCommon.HIGHEST, guildMember.uuid,
                        member -> this.addPlayer(guildMember, member));
                } else {
                    addPlayer(guildMember, player);
                }
            }
        });
    }


    private void addPlayer(WynnGuildMember guildMember, WynnPlayer player) {
        this.parent.addPlayer(guildMember, player);
        checkIsFinished();
    }

    private void checkIsFinished() {
        if (this.parent.isGuildMembersPresent()) {
            onFinishedProgress();
            editMessage();
        } else {
            editMessageOnTimer();
        }
    }

    @Override
    public void editMessage() {
        this.lastUpdated.set(System.currentTimeMillis());
        super.editMessage();
    }

    private void editMessageOnTimer() {
        long lastEditDifference = System.currentTimeMillis() - this.lastUpdated.get();
        if (lastEditDifference > 1500)
            this.editMessage();
    }

    public void onFinishedProgress() {
        this.onFinished.run();
    }

    @Override
    public MessageCreateData makeMessage() {
        double progress = this.parent.getMembersProgress();
        return buildCreate(Pretty.getProgress(progress));
    }
}
