package apple.discord.clover.discord.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface SendMessage {

    default EmbedBuilder success() {
        return new EmbedBuilder().setColor(CloverColor.SUCCESS);
    }

    default MessageEmbed success(String msg) {
        return success().setDescription(msg).build();
    }

    default EmbedBuilder error() {
        return new EmbedBuilder().setColor(CloverColor.BAD);
    }

    default MessageEmbed error(String msg) {
        return error()
            .setTitle("Error \u274C")
            .setDescription(msg)
            .build();
    }

}
