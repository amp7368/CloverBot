package apple.discord.clover.database.query.run;

import apple.discord.clover.database.player.run.DLevelupRun;
import io.avaje.lang.Nullable;
import io.ebean.DB;
import java.util.UUID;

public class RunStorage {

    private static final String MOST_RECENT_RUN_QUERY = """
        SELECT lr.*
        FROM play_session ps
                 LEFT JOIN (
                           SELECT *
                           FROM player_character
                           WHERE character_id = :character_id) pc
                           ON ps.id = pc.session_id
                 LEFT JOIN (
                           SELECT *
                           FROM levelup_run
                           WHERE name = :level_name) lr ON lr.character_sku = pc.sku
        WHERE lr.run_id IS NOT NULL
        ORDER BY retrieved_time DESC
        LIMIT 1;
        """;

    @Nullable
    public static DLevelupRun findRecentLevelRun(UUID characterId, String levelName) {
        return DB.findNative(DLevelupRun.class, MOST_RECENT_RUN_QUERY)
            .setParameter("character_id", characterId)
            .setParameter("level_name", levelName)
            .findOne();
    }
}
