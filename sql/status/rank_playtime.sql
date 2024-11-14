DROP TABLE tmp_tbl;
SELECT ps2.id
INTO TEMPORARY TABLE tmp_tbl
FROM play_session ps2
         JOIN (
              SELECT ps1.player_uuid, MIN(ps1.join_time) join_time
              FROM play_session ps1
              WHERE ps1.retrieved_time IS NOT NULL
              GROUP BY ps1.player_uuid) ps1
              ON ps1.player_uuid = ps2.player_uuid AND
                 timestamptz_eq(ps1.join_time, ps2.join_time);

SELECT DATE_TRUNC('MONTH', retrieved_time) AS     month,
       COUNT(id)                                  session_count,
       ROUND(SUM(pc.playtime_delta) / 60 / 60, 2) hours_playtime
FROM play_session ps
         LEFT JOIN player_character pc ON pc.session_id = ps.id
WHERE NOT EXISTS(
                SELECT 1
                FROM tmp_tbl
                WHERE tmp_tbl.id = pc.session_id)
  AND AGE(retrieved_time, date('2024-04-1'))
    BETWEEN INTERVAL '0 MONTH' AND INTERVAL '1 MONTH'
GROUP BY ps.player_uuid,
         DATE_TRUNC('MONTH', retrieved_time);


--
SELECT *
FROM (
     SELECT *,
            DENSE_RANK()
            OVER (PARTITION BY month ORDER BY hours_playtime DESC NULLS LAST) nth_player
     FROM (
          SELECT player.username,
                 CONCAT(DATE_PART('MONTH', q1.month),
                        '/',
                        DATE_PART('YEAR', q1.month)
                 ) AS month,
                 q1.hours_playtime,
                 q1.session_count,
                 q1.player_uuid
          FROM (
               SELECT DATE_TRUNC('MONTH', retrieved_time) AS month,
                      COUNT(id)                              session_count,
                      ROUND(SUM(pc.playtime_delta) / 60, 2)  hours_playtime,
                      player_uuid
               FROM play_session ps
                        LEFT JOIN player_character pc ON pc.session_id = ps.id
               WHERE AGE(ps.retrieved_time, date('2024-04-1'))
                   BETWEEN INTERVAL '0 MONTH' AND INTERVAL '1 MONTH'
                 AND NOT EXISTS(
                               SELECT 1
                               FROM tmp_tbl
                               WHERE tmp_tbl.id = pc.session_id)

               GROUP BY ps.player_uuid,
                        DATE_TRUNC('MONTH', retrieved_time)) q1
                   LEFT JOIN player ON player.uuid = q1.player_uuid) AS q2) q3
WHERE nth_player <= 100
ORDER BY nth_player;


SELECT retrieved_time, SUM(pc.playtime_snapshot) snapshot, SUM(pc.playtime_delta) delta
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
WHERE retrieved_time BETWEEN '2023-12-05 03:00:00' AND '2023-12-06'
GROUP BY retrieved_time
ORDER BY retrieved_time;


SELECT COUNT(id)                        session_count,
       ROUND(SUM(pc.playtime_delta), 2) hours_playtime
FROM play_session ps
         LEFT JOIN player_character pc ON pc.session_id = ps.id;

SELECT ps.playtime_snapshot, ps.retrieved_time
FROM play_session ps
         LEFT JOIN player_character pc ON pc.session_id = ps.id
ORDER BY ps.playtime_snapshot DESC
LIMIT 100;

SELECT pc.type, ps.retrieved_time, pc.playtime_snapshot, l.level_snapshot, l.name
FROM play_session ps
         LEFT JOIN player_character pc ON pc.session_id = ps.id
         LEFT JOIN levelup_run l ON l.character_sku = pc.sku
WHERE ps.playtime_snapshot = 18529
  AND l.name = 'combat'
  AND player_uuid = 'fee05d22-06b2-4479-9ca5-6fd4db985227'
ORDER BY level_snapshot DESC;

SELECT SUM(pc.playtime_delta) / 60 * 5.0
FROM play_session ps
         LEFT JOIN player_character pc ON pc.session_id = ps.id
WHERE ps.player_uuid = '2ced1b8e-d540-432f-8fd4-3874f7c36fc7';


SELECT MAX(ps.playtime_snapshot) * 5.0 * 60 playtime, player.username, ps.player_uuid
FROM play_session ps
         LEFT JOIN player ON ps.player_uuid = player.uuid
GROUP BY ps.player_uuid, player.username
HAVING MAX(ps.playtime_snapshot) > 10.916 * 60 / 5.0
   AND MAX(retrieved_time) > '12-10-01'
ORDER BY MAX(ps.playtime_snapshot);

DROP TABLE IF EXISTS tmp_tbl;