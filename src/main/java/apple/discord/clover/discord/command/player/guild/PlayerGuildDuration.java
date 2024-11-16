package apple.discord.clover.discord.command.player.guild;

import apple.discord.clover.database.guild.DGuild;
import apple.utilities.util.ObjectUtilsFormatting;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public record PlayerGuildDuration(DGuild guild, Instant from, Instant until, boolean isFirstGuild, boolean isLastGuild) {

    public String guildMsg() {
        String name = guild.isActive() ? guild.getName() : "~~Deleted~~";
        String tag = guild.getTag();

        String time = "<t:%d:D>";
        String fromString = isFirstGuild ? "`long ago`" : time.formatted(from.getEpochSecond());
        String untilString = isLastGuild ? "`now`" : time.formatted(until.getEpochSecond());

        return "### %s [%s]\n- from **%s** until **%s**".formatted(name, tag, fromString, untilString);
    }

    @Override
    public String toString() {
        String last = ObjectUtilsFormatting.failToNull(guild, DGuild::getName);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(ZoneId.systemDefault());
        String fromString = formatter.format(from());
        String untilString = formatter.format(until());
        return "%s [%s - %s]".formatted(last, fromString, untilString);
    }
}
