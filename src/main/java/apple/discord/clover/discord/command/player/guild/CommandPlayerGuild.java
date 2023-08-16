package apple.discord.clover.discord.command.player.guild;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import apple.discord.clover.database.query.player.PlayerGuildChange;
import apple.discord.clover.database.query.player.PlayerGuildQuery;
import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.slash.DCFSlashSubCommand;
import java.util.List;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class CommandPlayerGuild extends DCFSlashSubCommand implements FindOption {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("guilds", "Create a report of one player's guild history")
            .addOption(OptionType.STRING, "player", "The player to create a report of", true, true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        PlayerNameOrUUID player = findPlayer(event);
        if (player == null) return;

        List<PlayerGuildChange> history = PlayerGuildQuery.queryGuildHistory(player.uuid());
        PlayerGuildGui gui = new PlayerGuildGui(dcf, event::reply, history, player);
        gui.addPage(new PlayerGuildMessage(gui));
        gui.send();
    }
}
