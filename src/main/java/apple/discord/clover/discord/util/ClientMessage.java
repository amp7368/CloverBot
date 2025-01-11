package apple.discord.clover.discord.util;

import apple.discord.clover.api.base.request.PlayerNameOrUUID;
import java.util.UUID;
import net.dv8tion.jda.api.EmbedBuilder;

public class ClientMessage {

    public static String avatar(UUID uuid) {
        return "https://mc-heads.net/head/" + uuid;
    }

    public static EmbedBuilder user(EmbedBuilder embed, PlayerNameOrUUID player) {
        return embed.setTitle(player.username())
            .setThumbnail(ClientMessage.avatar(player.uuid()));
    }

    public static EmbedBuilder user(PlayerNameOrUUID player) {
        return user(new EmbedBuilder(), player);
    }
}
