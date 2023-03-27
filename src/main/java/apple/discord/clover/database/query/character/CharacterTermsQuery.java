package apple.discord.clover.database.query.character;

import apple.discord.clover.api.character.term.request.CharacterRequest;
import apple.discord.clover.api.character.term.response.CharacterTerm;
import io.ebean.DB;
import java.util.List;
import java.util.UUID;

public class CharacterTermsQuery {

    public static final String CHARACTER_TERMS_QUERY = """
        SELECT r.*
        FROM (
             SELECT DATE_TRUNC(:resolution, retrieved_time) AS retrieved,
                    SUM(pc.playtime_delta)             AS playtime_delta,
                    SUM(pc.items_identified_delta)     AS items_identified_delta,
                    SUM(pc.mobs_killed_delta)          AS mobs_killed_delta,
                    SUM(pc.blocks_walked_delta)        AS blocks_walked_delta,
                    SUM(pc.logins_delta)               AS logins_delta,
                    SUM(pc.deaths_delta)               AS deaths_delta
             FROM player_character pc
                      LEFT JOIN play_session s
                                ON pc.session_id = s.id
             WHERE pc.character_id = :character
               AND s.retrieved_time BETWEEN date(:start) AND date(:end)
             GROUP BY retrieved
             ORDER BY retrieved) r
        WHERE playtime_delta != 0
           OR items_identified_delta != 0
           OR mobs_killed_delta != 0
           OR blocks_walked_delta != 0
           OR logins_delta != 0
           OR deaths_delta != 0;
           """;


    public static List<CharacterTerm> queryCharacterTerms(CharacterRequest request, UUID id) {
        return DB.findDto(CharacterTerm.class, CHARACTER_TERMS_QUERY)
            .setParameter("resolution", request.getTimeResolution().sql())
            .setParameter("character", id)
            .setParameter("start", request.startSql())
            .setParameter("end", request.endSql()).findList();
    }
}
