UPDATE player_character pco
SET playtime_delta =
        pco.playtime_snapshot -
        (
        SELECT (CASE WHEN pc.playtime_snapshot IS NULL THEN 0 ELSE pc.playtime_snapshot END)
        FROM play_session ps
                 LEFT JOIN player_character pc ON ps.id = pc.session_id
        WHERE q1.player_uuid = ps.player_uuid
          AND retrieved_time < '2023-12-01 05:41:54.000000 +00:00'
          AND pco.character_id = pc.character_id
        ORDER BY retrieved_time DESC
        LIMIT 1)
FROM (
     SELECT player_uuid, MIN(retrieved_time) min_time
     FROM play_session
     WHERE retrieved_time BETWEEN '2023-12-01 05:41:54.000000 +00:00' AND '2023-12-05 09:35:47.000000 +00:00'
     GROUP BY player_uuid) q1
         LEFT JOIN
     play_session ps ON q1.player_uuid = ps.player_uuid AND q1.min_time = ps.retrieved_time
WHERE pco.session_id = ps.id;

SET LOCK_TIMEOUT = 0;



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

SELECT SUM(pc.playtime_delta),
       ps.playtime_snapshot ps_snap,
       retrieved_time,
       player.username,
       session_id
FROM player_character pc
         LEFT JOIN play_session ps ON ps.id = pc.session_id
         LEFT JOIN player ON ps.player_uuid = player.uuid
WHERE player_uuid = '26eba7b3-c315-4ba5-b548-7781577021fc'
GROUP BY retrieved_time, ps.playtime_snapshot, session_id, player.username
ORDER BY retrieved_time DESC;

SELECT playtime_snapshot, character_id
FROM player_character pc
WHERE session_id IN ('ce28db0e-55e4-4a7f-9da0-9b35648be6bd')
ORDER BY session_id, character_id;
