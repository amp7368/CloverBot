package apple.discord.clover.database.query.character;

import apple.discord.clover.api.character.request.CharacterRequest;
import apple.discord.clover.api.character.response.CharacterStats;
import apple.discord.clover.api.character.response.CharacterStatsListResponse;
import apple.discord.clover.api.character.response.CharacterTerm;
import apple.discord.clover.database.character.DCharacter;
import apple.discord.clover.database.character.query.QDCharacter;
import io.ebean.DB;
import java.util.List;
import java.util.UUID;

public class CharacterTermsQuery {

    public static CharacterStatsListResponse queryCharacterTerms(CharacterRequest request) {
        CharacterStatsListResponse response = new CharacterStatsListResponse(request.start(), request.end());
        List<UUID> characters = queryCharacterIds(request.player);
        for (UUID character : characters) {
            response.addCharacter(queryCharacter(request, character));
        }
        return response;
    }

    private static CharacterStats queryCharacter(CharacterRequest request, UUID id) {
        CharacterStats character = new CharacterStats(id);
        character.setTerms(queryTerms(request, id));
        character.setFirst(queryFirstTerm(request, id));
        character.setLast(queryLastTerm(request, id));
        return character;
    }

    private static DCharacter queryFirstTerm(CharacterRequest request, UUID id) {
        return queryTerm(request, id)
            .orderBy().session.retrievedTime.asc()
            .findOne();
    }

    private static DCharacter queryLastTerm(CharacterRequest request, UUID id) {
        return queryTerm(request, id)
            .orderBy().session.retrievedTime.desc()
            .findOne();
    }

    private static QDCharacter queryTerm(CharacterRequest request, UUID id) {
        return new QDCharacter()
            .where().and()
            .characterId.eq(id)
            .session.retrievedTime.between(request.startSql(), request.endSql())
            .endAnd()
            .setMaxRows(1);
    }

    private static List<CharacterTerm> queryTerms(CharacterRequest request, UUID id) {
        return DB.findDto(CharacterTerm.class,
                """
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
                       """)
            .setParameter("resolution", request.timeResolution.sql())
            .setParameter("character", id)
            .setParameter("start", request.startSql())
            .setParameter("end", request.endSql()).findList();
    }

    private static List<UUID> queryCharacterIds(UUID playerId) {
        QDCharacter a = QDCharacter.alias();
        return new QDCharacter()
            .select(a.characterId)
            .setDistinct(true)
            .where().session.player.uuid.eq(playerId)
            .findSingleAttributeList();
    }

}
