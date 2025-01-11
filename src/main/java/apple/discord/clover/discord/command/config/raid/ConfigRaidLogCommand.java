package apple.discord.clover.discord.command.config.raid;

import apple.discord.clover.discord.util.FindOption;
import discord.util.dcf.gui.base.edit_message.DCFEditMessage;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.slash.DCFSlashSubCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ConfigRaidLogCommand extends DCFSlashSubCommand implements FindOption {

    @Override
    public SubcommandData getData() {
        return new SubcommandData("raid_log", "Configure logging raids");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        DCFGui gui = new DCFGui(dcf, DCFEditMessage.ofReply(event::reply));
        new ConfigRaidLogPage(gui)
            .addPageToGui()
            .send();
    }
}
