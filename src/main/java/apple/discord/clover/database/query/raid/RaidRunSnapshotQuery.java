package apple.discord.clover.database.query.raid;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import apple.discord.clover.api.character.raid.request.CharacterRaidRequest;
import apple.discord.clover.api.character.raid.response.snapshot.PlayerRaidCharactersSnapshot;
import apple.discord.clover.api.character.raid.response.snapshot.PlayerRaidSnapshot;
import io.ebean.DB;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class RaidRunSnapshotQuery {

    private final static String RAID_FIRST_SNAPSHOT = """
        SELECT ps.retrieved_time    AS retrieved,
               rr.runs_snapshot,
               rr.runs_delta,
               :raid_name           AS raid,
               :character_id        AS character_id
        FROM play_session ps
                 LEFT JOIN player_character pc ON ps.id = pc.session_id
                 LEFT JOIN raid_run rr ON pc.sku = rr.character_sku
        WHERE rr.name = :raid_name
          AND pc.character_id = :character_id
          AND retrieved_time < date(:end)
        ORDER BY CASE
                     WHEN ps.retrieved_time >= date(:start)
                         THEN ps.retrieved_time
                     ELSE date(:end) END
               , ps.retrieved_time DESC
        LIMIT 1;
        """;
    private final static String RAID_LAST_SNAPSHOT = """
        SELECT ps.retrieved_time    AS retrieved,
               rr.runs_snapshot,
               rr.runs_delta,
               :raid_name           AS raid,
               :character_id        AS character_id
        FROM play_session ps
                 LEFT JOIN player_character pc ON ps.id = pc.session_id
                 LEFT JOIN raid_run rr ON pc.sku = rr.character_sku
        WHERE rr.name = :raid_name
          AND pc.character_id = :character_id
          AND ps.retrieved_time < date(:end)
        ORDER BY ps.retrieved_time DESC
        LIMIT 1;
        """;

    public static PlayerRaidSnapshot queryRaidsSnapshot(CharacterRaidRequest request, List<UUID> characterIds,
        List<String> raidNames, boolean isFirst) {
        int queryCount = characterIds.size() * raidNames.size();
        List<CompletableFuture<RaidRunRaw>> queries = new ArrayList<>(queryCount);
        for (UUID character : characterIds) {
            for (String raid : raidNames) {
                Supplier<RaidRunRaw> query = isFirst
                    ? () -> queryOneFirst(request, character, raid)
                    : () -> queryOneLast(request, character, raid);

                queries.add(supplyAsync(query));
            }
        }
        allOf(queries.toArray(CompletableFuture[]::new)).join();
        Instant snapShotAt = isFirst ? request.start() : request.end();
        PlayerRaidSnapshot response = new PlayerRaidSnapshot(snapShotAt);
        List<RaidRunRaw> results = queries.stream().map(q -> q.getNow(null)).toList();
        for (RaidRunRaw result : results) {
            if (result == null) continue;

            response.add(result.runsSnapshotAt(snapShotAt));

            PlayerRaidCharactersSnapshot raid = response.getOrCreateRaid(result.raid);
            raid.setCharacter(result.characterId, result.runsSnapshotAt(snapShotAt));
        }
        return response;
    }

    private static RaidRunRaw queryOneFirst(CharacterRaidRequest request, UUID character, String raid) {
        return DB.findDto(RaidRunRaw.class, RAID_FIRST_SNAPSHOT)
            .setParameter("character_id", character)
            .setParameter("raid_name", raid)
            .setParameter("start", request.startSql())
            .setParameter("end", request.endSql()).findOne();
    }

    private static RaidRunRaw queryOneLast(CharacterRaidRequest request, UUID character, String raid) {
        return DB.findDto(RaidRunRaw.class, RAID_LAST_SNAPSHOT)
            .setParameter("character_id", character)
            .setParameter("raid_name", raid)
            .setParameter("end", request.endSql()).findOne();
    }
}
