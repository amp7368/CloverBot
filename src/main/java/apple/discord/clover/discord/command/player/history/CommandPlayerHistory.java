package apple.discord.clover.discord.command.player.history;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.database.player.DPlayer;
import apple.discord.clover.database.player.PlayerStorage;
import apple.discord.clover.discord.command.activity.base.player.InactiveDPlayer;
import apple.discord.clover.discord.command.activity.base.player.InactiveNotFoundPlayer;
import apple.discord.clover.discord.command.activity.base.player.InactivePlayer;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandPlayerHistory extends DCFSlashSubCommand implements FindOption {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("history", "Create a report of one player's playtime")
            .addOption(OptionType.STRING, "player", "The player to create a report of", true, true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        PlayerNameOrUUID playerName = findPlayer(event);
        if (playerName == null) return;

        DPlayer dPlayer = PlayerStorage.findPlayer(playerName.uuid());
        InactivePlayer inactivePlayer = dPlayer == null ?
            new InactiveNotFoundPlayer(null) :
            new InactiveDPlayer(null, dPlayer);

        PlayerHistoryGui gui = new PlayerHistoryGui(dcf, event::reply, dPlayer, inactivePlayer, playerName);
        gui.addPage(new PlayerHistoryMessage(gui));
        gui.send();
    }
}
