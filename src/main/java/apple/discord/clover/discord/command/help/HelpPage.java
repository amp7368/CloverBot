package apple.discord.clover.discord.command.help;

import apple.discord.clover.discord.DiscordModule;
import discord.util.dcf.gui.base.gui.DCFGui;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class HelpPage extends DCFGuiPage<DCFGui> {

    private final MessageEmbed pageData;

    public HelpPage(DCFGui dcfGui, MessageEmbed pageData) {
        super(dcfGui);
        this.pageData = pageData;
    }

    @Override
    public MessageCreateData makeMessage() {
        MessageCreateBuilder builder = new MessageCreateBuilder();
        EmbedBuilder embed = new EmbedBuilder(pageData);
        embed.setTitle("(Page %d)".formatted(getPageNum() + 1));
        embed.setThumbnail(DiscordModule.dcf.jda().getSelfUser().getEffectiveAvatarUrl());
        builder.setEmbeds(embed.build());
        builder.setActionRow(btnFirst(), btnLast(), btnNext(), Button.link(DiscordModule.INVITE_LINK, "Invite"));
        return builder.build();
    }
}
