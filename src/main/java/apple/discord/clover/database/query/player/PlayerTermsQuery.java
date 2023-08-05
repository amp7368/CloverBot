package apple.discord.clover.database.query.player;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import apple.discord.clover.api.player.terms.request.PlayerTermsRequest;
import apple.discord.clover.api.player.terms.response.PlaySessionTerm;
import apple.discord.clover.api.player.terms.response.PlayerTermsResponse;
import apple.discord.clover.database.activity.DPlaySession;
import apple.discord.clover.database.activity.query.QDPlaySession;
import io.ebean.DB;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;

public class PlayerTermsQuery {

    public static PlayerTermsResponse queryPlayerTerms(PlayerTermsRequest request) {
        PlayerTermsResponse response = new PlayerTermsResponse(request.start(), request.end());

        CompletableFuture<List<PlaySessionTerm>> terms = supplyAsync(() -> queryTerms(request));
        CompletableFuture<DPlaySession> first = supplyAsync(() -> queryFirstTerm(request));
        CompletableFuture<DPlaySession> last = supplyAsync(() -> queryLastTerm(request));
        allOf(terms, first, last).join();
        response.setTerms(terms.getNow(null));
        response.setFirst(first.getNow(null), request.start());
        response.setLast(last.getNow(null));
        return response;
    }

    private static DPlaySession queryFirstTerm(PlayerTermsRequest request) {
        return queryTerm(request)
            .alias("ps")
            .orderBy(
                """
                    CASE
                          WHEN ps.retrieved_time >= date('%s')
                              THEN ps.retrieved_time
                          ELSE date('%s') END
                    , ps.retrieved_time DESC
                    """.formatted(request.startSql(), request.endSql())
            )
            .findOne();
    }

    private static DPlaySession queryLastTerm(PlayerTermsRequest request) {
        return queryTerm(request)
            .orderBy().retrievedTime.desc()
            .findOne();
    }

    @NotNull
    private static QDPlaySession queryTerm(PlayerTermsRequest request) {
        return new QDPlaySession().where().and()
            .player.uuid.eq(request.getPlayer().uuid())
            .endAnd()
            .setMaxRows(1);
    }

    @NotNull
    private static List<PlaySessionTerm> queryTerms(PlayerTermsRequest request) {
        return DB.findDto(PlaySessionTerm.class, """
                SELECT DATE_TRUNC(:resolution, retrieved_time) AS retrieved,
                       SUM(c.playtime_delta)                   AS playtime_delta,
                       SUM(combat_delta)                       AS combat_delta,
                       SUM(c.items_identified_delta)           AS items_identified_delta,
                       SUM(c.mobs_killed_delta)                AS mobs_killed_delta,
                       SUM(prof_delta)                         AS total_prof_level_delta
                FROM play_session s
                         LEFT JOIN player_character c ON s.id = c.session_id
                WHERE player_uuid = :player
                  AND retrieved_time BETWEEN date(:start) AND date(:end)
                GROUP BY retrieved
                ORDER BY retrieved;
                """)
            .setParameter("resolution", request.getTimeResolution().sql())
            .setParameter("player", request.getPlayer().uuid())
            .setParameter("start", request.startSql())
            .setParameter("end", request.endSql()).findList();
    }
}
