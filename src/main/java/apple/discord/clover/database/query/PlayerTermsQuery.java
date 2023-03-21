package apple.discord.clover.database.query;

import apple.discord.clover.api.player.activity.PlaySessionTerm;
import apple.discord.clover.api.player.activity.PlayerRequest;
import apple.discord.clover.api.player.activity.PlayerResponse;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.query.QDPlaySession;
import io.ebean.DB;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PlayerTermsQuery {

    public static PlayerResponse queryPlayerTerms(PlayerRequest request) {
        PlayerResponse response = new PlayerResponse(request.start(), request.end());
        response.setTerms(queryTerms(request));
        response.setFirst(queryFirstTerm(request));
        response.setLast(queryLastTerm(request));
        return response;
    }

    private static DPlaySession queryFirstTerm(PlayerRequest request) {
        return queryTerm(request)
            .orderBy().retrievedTime.asc()
            .findOne();
    }

    private static DPlaySession queryLastTerm(PlayerRequest request) {
        return queryTerm(request)
            .orderBy().retrievedTime.desc()
            .findOne();
    }

    @NotNull
    private static QDPlaySession queryTerm(PlayerRequest request) {
        return new QDPlaySession().where().and()
            .player.uuid.eq(request.player)
            .retrievedTime.between(request.startSql(), request.endSql())
            .endAnd()
            .setMaxRows(1);
    }

    @NotNull
    private static List<PlaySessionTerm> queryTerms(PlayerRequest request) {
        return DB.findDto(PlaySessionTerm.class, """
                select DATE_TRUNC(:resolution, retrieved_time)  AS retrieved,
                     SUM(playtime_delta)                  AS playtime_delta,
                     SUM(combat_delta)                    AS combat_delta,
                     SUM(items_identified_delta)          AS items_identified_delta,
                     SUM(mobs_killed_delta)               AS mobs_killed_delta,
                     SUM(prof_delta)                      AS total_prof_level_delta
                FROM play_session s
                WHERE player_uuid = :player
                  AND retrieved_time BETWEEN date(:start) AND date(:end)
                GROUP BY retrieved
                ORDER BY retrieved
                """)
            .setParameter("resolution", request.timeResolution.sql())
            .setParameter("player", request.player)
            .setParameter("start", request.startSql())
            .setParameter("end", request.endSql()).findList();
    }
}
