package apple.discord.clover.database.query.raid;

import apple.discord.clover.api.character.raid.request.CharacterRaidRequest;
import apple.discord.clover.api.character.raid.response.term.PlayerRaidCharactersTerm;
import apple.discord.clover.api.character.raid.response.term.PlayerRaidTerm;
import apple.discord.clover.api.character.raid.response.term.RaidDelta;
import io.ebean.DB;
import java.util.ArrayList;
import java.util.List;

public class RaidRunTermsQuery {

    private static final String RAID_TERMS_QUERY = """
        SELECT DATE_TRUNC(:resolution, retrieved_time) AS retrieved,
               SUM(rr.runs_delta) AS runs_delta,
               rr.name as raid,
               pc.character_id
        FROM raid_run rr
                 LEFT JOIN player_character pc ON pc.sku = rr.character_sku
                 LEFT JOIN play_session s ON pc.session_id = s.id
        WHERE pc.character_id
            IN (
               SELECT DISTINCT character_id
               FROM play_session s
                        LEFT JOIN player_character pc ON s.id = pc.session_id
               WHERE player_uuid = :player)
          AND s.retrieved_time BETWEEN date(:start) AND date(:end)
          AND rr.runs_delta != 0
        GROUP BY retrieved, rr.name, pc.type, pc.character_id
        ORDER BY retrieved;
        """;

    public static List<PlayerRaidTerm> raidRunQueryTerms(CharacterRaidRequest request) {
        List<RaidRunRaw> query = DB.findDto(RaidRunRaw.class, RAID_TERMS_QUERY)
            .setParameter("resolution", request.getTimeResolution().sql())
            .setParameter("player", request.player)
            .setParameter("start", request.startSql())
            .setParameter("end", request.endSql()).findList();
        List<PlayerRaidTerm> terms = new ArrayList<>();
        PlayerRaidTerm term = null;
        for (RaidRunRaw row : query) {
            if (term == null || !term.retrieved.equals(row.retrieved)) {
                term = new PlayerRaidTerm(row.retrieved);
                terms.add(term);
            }
            PlayerRaidCharactersTerm raid = term.getOrCreateRaid(row.raid);
            raid.setCharacter(row.characterId, new RaidDelta(row.runsDelta));
        }
        return terms;
    }

}
