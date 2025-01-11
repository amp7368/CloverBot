package apple.discord.clover.discord.command.config.raid;

import apple.discord.clover.discord.system.theme.CloverColor;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class ConfigRaidLogPage extends DCFGuiPage<DCFGui> {

    public ConfigRaidLogPage(DCFGui parent) {
        super(parent);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(CloverColor.BLUE_SPECIAL);
        embed.appendDescription("# Raid Log Configuration");
        return null;
    }
}
