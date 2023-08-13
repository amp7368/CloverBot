package apple.discord.clover.discord.command.player.history;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.database.query.player.PlayerStatsQuery;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import org.jetbrains.annotations.Nullable;

public class PlayerHistoryGui extends DCFGui {

    private final DPlayer dPlayer;
    private final InactivePlayer inactivePlayer;
    private final PlayerNameOrUUID playerName;
    private final ListenableFuture<DPlaySession> lastSession;

    public PlayerHistoryGui(DCF dcf, GuiReplyFirstMessage createFirstMessage,
        DPlayer dPlayer,
        InactivePlayer inactivePlayer,
        PlayerNameOrUUID playerName) {
        super(dcf, createFirstMessage);
        this.dPlayer = dPlayer;
        this.inactivePlayer = inactivePlayer;
        this.playerName = playerName;
        this.lastSession = Futures.submit(() -> PlayerStatsQuery.findLastSession(this.dPlayer), ForkJoinPool.commonPool());
    }

    public PlayerNameOrUUID getPlayerName() {
        return this.playerName;
    }

    public InactivePlayer getInactive() {
        return this.inactivePlayer;
    }

    @Nullable
    public DGuild getGuild() {
        return lastPlaySession().map(s -> s.guild).orElse(null);
    }

    private Optional<DPlaySession> lastPlaySession() {
        try {
            return Optional.of(lastSession.get());
        } catch (InterruptedException | ExecutionException e) {
            return Optional.empty();
        }
    }
}
