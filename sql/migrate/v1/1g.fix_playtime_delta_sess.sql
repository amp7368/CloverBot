SET LOCK_TIMEOUT = 0;
UPDATE play_session pso
SET playtime_delta =
        pso.playtime_snapshot -
        (
        SELECT COALESCE(
                       (
                       SELECT ps.playtime_snapshot
                       FROM play_session ps
                       WHERE q1.player_uuid = ps.player_uuid
                         AND retrieved_time < q1.retrieved_time
                       ORDER BY retrieved_time DESC
                       LIMIT 1), 0))
FROM (
     SELECT ps.player_uuid, ps.id, retrieved_time
     FROM player_character pc
              LEFT JOIN play_session ps ON ps.id = pc.session_id
     WHERE pc.playtime_delta < 0
        OR pc.playtime_delta IS NULL) q1
WHERE pso.id = q1.id;



SELECT *
FROM (
     SELECT player_uuid, MAX(retrieved_time) max_time
     FROM play_session
     WHERE retrieved_time < '2023-12-01 05:41:54.000000 +00:00'
     GROUP BY player_uuid) q1
         LEFT JOIN
     play_session ps ON q1.player_uuid = ps.player_uuid AND q1.max_time = ps.retrieved_time;

SELECT ps.playtime_snapshot, ps.playtime_delta, retrieved_time, ps.id, player_uuid
FROM play_session ps
WHERE player_uuid = (
                    SELECT player_uuid
                    FROM play_session ps3
                    WHERE ps3.id = 'ce28db0e-55e4-4a7f-9da0-9b35648be6bd')
ORDER BY retrieved_time DESC;

