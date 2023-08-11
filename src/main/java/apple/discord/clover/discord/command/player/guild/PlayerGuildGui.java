package apple.discord.clover.discord.command.player.guild;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.database.query.player.PlayerGuildChange;
import discord.util.dcf.DCF;
import discord.util.dcf.gui.base.GuiReplyFirstMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import java.util.Collection;
import java.util.List;

public class PlayerGuildGui extends DCFGui {

    private final List<PlayerGuildChange> guildHistory;
    private final PlayerNameOrUUID player;

    public PlayerGuildGui(DCF dcf, GuiReplyFirstMessage createFirstMessage,
        List<PlayerGuildChange> guildHistory,
        PlayerNameOrUUID player) {
        super(dcf, createFirstMessage);
        
        this.guildHistory = guildHistory;
        this.player = player;
    }

    public Collection<PlayerGuildChange> getGuildHistory() {
        return guildHistory;
    }

    public PlayerNameOrUUID getPlayer() {
        return this.player;
    }
}
