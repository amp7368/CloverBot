SELECT player_count,
       ('{Mon,Tue,Wed,Thu,Fri,Sat,Sun}'::TEXT[])[EXTRACT(ISODOW FROM retrieved)] day_of_week,
       retrieved
FROM (
     SELECT DATE_TRUNC('DAY', retrieved_time) retrieved,
            COUNT(DISTINCT player_uuid)       player_count
     FROM play_session ps
     GROUP BY retrieved) q
ORDER BY retrieved DESC;


SELECT ps2.id
INTO tmp_tbl
FROM play_session ps2
         JOIN (
              SELECT ps1.player_uuid, MIN(ps1.join_time) join_time
              FROM play_session ps1
              WHERE ps1.retrieved_time IS NOT NULL
              GROUP BY ps1.player_uuid) ps1
              ON ps1.player_uuid = ps2.player_uuid AND
                 timestamptz_eq(ps1.join_time, ps2.join_time);

SELECT player_playtime,
       ('{Mon,Tue,Wed,Thu,Fri,Sat,Sun}'::TEXT[])[EXTRACT(ISODOW FROM retrieved)] day_of_week,
       retrieved
FROM (
     SELECT DATE_TRUNC('DAY', retrieved_time) retrieved,
            SUM(pc.playtime_delta) * 4.7 / 60 player_playtime
     FROM (
          SELECT *
          FROM player_character pc
          WHERE NOT EXISTS  (
                            SELECT 1
                            FROM tmp_tbl
                            WHERE tmp_tbl.id = pc.session_id)) pc
              LEFT JOIN play_session ps ON pc.session_id = ps.id
     GROUP BY retrieved) q
ORDER BY retrieved;

SELECT *
FROM play_session ps
WHERE ps.player_uuid = '2ecb1026-9fb2-4f6c-b37b-e4999390f7b9';


