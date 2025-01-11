package apple.discord.clover.discord.util;

import apple.discord.clover.discord.system.theme.CloverColor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public interface SendMessage {

    static SendMessage get() {
        return new SendMessage() {
        };
    }

    default EmbedBuilder success() {
        return new EmbedBuilder().setColor(CloverColor.GREEN);
    }

    default MessageEmbed success(String msg) {
        return success().setDescription(msg).build();
    }

    default EmbedBuilder error() {
        return new EmbedBuilder().setColor(CloverColor.RED);
    }

    default MessageEmbed error(String msg) {
        return error()
            .setTitle("Error \u274C")
            .setDescription(msg)
            .build();
    }

}
