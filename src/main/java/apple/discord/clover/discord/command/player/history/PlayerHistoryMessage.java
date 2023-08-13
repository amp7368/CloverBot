package apple.discord.clover.discord.command.player.history;

import apple.discord.clover.api.base.request.TimeResolution;
import apple.discord.clover.database.player.guild.DGuild;
import apple.discord.clover.discord.util.CloverColor;
import apple.discord.clover.discord.util.DiscordPlayerUtils;
import discord.util.dcf.gui.base.page.DCFGuiPage;
import java.time.Duration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class PlayerHistoryMessage extends DCFGuiPage<PlayerHistoryGui> {

    public PlayerHistoryMessage(PlayerHistoryGui parent) {
        super(parent);
    }

    protected static String displayHours(Duration playtime) {
        if (playtime == null) return "??? Hours";
        long minutes = playtime.toMinutes();
        double hours = minutes / 60.0;
        return "%.1f Hours".formatted(hours);
    }

    @Override
    public MessageCreateData makeMessage() {
        EmbedBuilder embed = DiscordPlayerUtils.user(parent.getPlayerName());
        embed.setColor(CloverColor.PLAYER)
            .setAuthor("Player History");

        StringBuilder desc = new StringBuilder();

        DGuild guild = parent.getGuild();
        if (guild == null) desc.append("### No Guild");
        else desc.append("### %s [%s]".formatted(guild.getName(), guild.getTag()));

        embed.setDescription(desc);

        String veryLongPlaytime = getPlaytime(TimeResolution.MONTH, 3);
        String longPlaytime = getPlaytime(TimeResolution.MONTH, 1);
        String moderatePlaytime = getPlaytime(TimeResolution.WEEK, 2);
        String shortPlaytime = getPlaytime(TimeResolution.WEEK, 1);
        embed.addField(veryLongPlaytime, "3 Month Playtime", true);
        embed.addField(longPlaytime, "1 Month Playtime", true);
        embed.addBlankField(true);
        embed.addField(moderatePlaytime, "2 Week Playtime", true);
        embed.addField(shortPlaytime, "1 Week Playtime", true);
        embed.addBlankField(true);

        return buildCreate(embed.build());
    }

    private String getPlaytime(TimeResolution resolution, int termsAfter) {
        return displayHours(parent.getInactive().getPlaytime(resolution, termsAfter));
    }
}
