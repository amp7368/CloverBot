package apple.discord.clover.discord.inactivity;

import apple.discord.acd.gui.awd.page.AWDPageGui;
import apple.discord.clover.discord.DiscordBot;
import apple.discord.clover.util.Pretty;
import apple.discord.clover.wynncraft.WynnDatabase;
import apple.discord.clover.wynncraft.WynnRequestPriority;
import apple.discord.clover.wynncraft.WynncraftService;
import apple.discord.clover.wynncraft.guild.WynnGuildMember;
import apple.discord.clover.wynncraft.player.WynnPlayer;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MessageInactivityProgress extends AWDPageGui<GuiInactivity> {
    private final AtomicLong lastUpdated = new AtomicLong(System.currentTimeMillis());
    private final Runnable onFinished;

    public MessageInactivityProgress(GuiInactivity parent, Runnable onFinished) {
        super(parent);
        this.onFinished = onFinished;
    }

    @Override
    public void makeFirstMessage() {
        super.makeFirstMessage();
        WynncraftService.queue(WynnRequestPriority.PRIMARY, parent.getGuildName(), wynnGuild -> {
            this.parent.setGuild(wynnGuild);
            editMessageOnTimer();
            if (wynnGuild == null) {
                return;
            }
            for (WynnGuildMember guildMember : List.of(wynnGuild.members)) {
                if (guildMember == null) continue;
                @Nullable WynnPlayer player = WynnDatabase.getPlayer(guildMember.uuid);
                if (player == null) {
                    WynncraftService.queuePriority(WynnRequestPriority.NOW, guildMember.uuid, member -> this.addPlayer(guildMember, member));
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
        long lastEditDifference = System.currentTimeMillis() - this.lastUpdated.getPlain();
        if (lastEditDifference > 1500) this.editMessage();
    }

    public void onFinishedProgress() {
        this.onFinished.run();
    }

    @Override
    public Message makeMessage() {
        double progress = this.parent.getMembersProgress();
        return new MessageBuilder(Pretty.getProgress(progress)).build();
    }
}
