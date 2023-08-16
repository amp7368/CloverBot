package apple.discord.clover.database.query.player;

import io.ebean.DB;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerGuildQuery {

    public static final String GUILD_CHANGE_QUERY = """
        SELECT last_guild, current_guild, retrieved_time
        FROM (
             SELECT LAG(guild_id, 1)
                    OVER (ORDER BY retrieved_time) last_guild,
                    guild_id                       current_guild,
                    retrieved_time
             FROM play_session
             WHERE player_uuid = :player
             ORDER BY retrieved_time) sess
        WHERE current_guild IS DISTINCT FROM last_guild""";

    public static List<PlayerGuildChange> queryGuildHistory(UUID player) {
        List<PlayerGuildChange> rawGuildChanges = DB.findDto(PlayerGuildChange.class, GUILD_CHANGE_QUERY)
            .setParameter("player", player)
            .findList();

        List<PlayerGuildChange> guildChanges = new ArrayList<>((int) (rawGuildChanges.size() * 1.25));
        for (PlayerGuildChange change : rawGuildChanges) {
            boolean hasNullValue = change.getCurrentGuild() == null || change.getLastGuild() == null;
            if (hasNullValue) {
                guildChanges.add(change);
                continue;
            }
            Instant guildLeaveTime = change.getRetrievedTime();
            PlayerGuildChange guildLeave = new PlayerGuildChange(change.lastGuild, null, Timestamp.from(guildLeaveTime));

            // make sure join comes after leave
            Timestamp guildJoinTime = Timestamp.from(guildLeaveTime.plusSeconds(1));
            PlayerGuildChange guildJoin = new PlayerGuildChange(null, change.currentGuild, guildJoinTime);

            guildChanges.add(guildLeave);
            guildChanges.add(guildJoin);
        }
        return guildChanges;
    }
}
