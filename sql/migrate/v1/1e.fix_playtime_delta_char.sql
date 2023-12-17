SET LOCK_TIMEOUT = 0;
EXPLAIN
UPDATE player_character pco
SET playtime_delta =
        pco.playtime_snapshot -
        (
        SELECT COALESCE((
                        SELECT pc.playtime_snapshot
                        FROM play_session ps
                                 LEFT JOIN player_character pc ON ps.id = pc.session_id
                            AND pc.character_id = pco.character_id
                        WHERE ps.player_uuid = q1.player_uuid
                          AND retrieved_time < q1.retrieved_time
                        ORDER BY retrieved_time DESC
                        LIMIT 1), 0))
FROM (
     SELECT ps.player_uuid, ps.id, retrieved_time
     FROM play_session ps
--               LEFT JOIN player_character pc ON ps.id = pc.session_id
     WHERE retrieved_time > '2023-12-01 05:41:55.000000 +00:00') q1
WHERE pco.session_id = q1.id;



SELECT ps.playtime_snapshot, ps.playtime_delta
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
WHERE ps.player_uuid = '40241e88-d49d-4e31-aad4-351a021a0a68'
ORDER BY retrieved_time DESC;


SELECT ps.playtime_snapshot, ps.playtime_delta, retrieved_time, ps.id, player_uuid
FROM play_session ps
WHERE player_uuid = (
                    SELECT player_uuid
                    FROM play_session ps3
                    WHERE ps3.id = 'ce2f9d07-30c5-44d1-b924-f44fc76bd8d3')
ORDER BY retrieved_time DESC;

SELECT ps.player_uuid, ps.id, retrieved_time
FROM player_character pc
         LEFT JOIN play_session ps ON ps.id = pc.session_id
WHERE pc.playtime_delta < 0
   OR pc.playtime_delta IS NULL;

SELECT *
FROM play_session ps
         LEFT JOIN public.player_character pc ON ps.id = pc.session_id
WHERE ps.player_uuid = '3539cefa-1265-4c0b-b114-e8be5909dfbe'
  AND pc.playtime_delta IS NULL
ORDER BY retrieved_time DESC;


SELECT SUM(pc.playtime_delta)    pc_delta,
       ps.playtime_delta         ps_delta,
       SUM(pc.playtime_snapshot) pc_snap,
       ps.playtime_snapshot      ps_snap,
       retrieved_time,
       player.username,
       session_id
FROM player_character pc
         LEFT JOIN play_session ps ON ps.id = pc.session_id
         LEFT JOIN player ON ps.player_uuid = player.uuid
WHERE username = 'Keldorn'
-- WHERE player_uuid = '01dfefc5-0fdb-48f4-b53f-2317ae5305bd'
GROUP BY retrieved_time, ps.playtime_snapshot, session_id, player.username, ps.playtime_delta
ORDER BY retrieved_time DESC;

SELECT playtime_snapshot, character_id
FROM player_character pc
WHERE session_id IN ('62cf08b0-1abd-4201-9f26-15ee3441791e', 'dee9c098-2b7f-4cd8-b15b-0dffd52c2c10')
ORDER BY session_id, character_id;
