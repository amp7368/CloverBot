SELECT SUM(pc.playtime_snapshot), session_id, retrieved_time
FROM player_character pc
         LEFT JOIN public.play_session ps ON pc.session_id = ps.id
WHERE retrieved_time >= '2023-12-01 05:41:55.000000 +00:00'
GROUP BY session_id, retrieved_time;


SELECT ps.player_uuid,
       pc.session_id,
       SUM(pc.playtime_snapshot) playtime_sum
INTO tmp_play_session_sum
FROM player_character pc
         JOIN (
              SELECT ps1.id, player_uuid
              FROM play_session ps1
              WHERE ps1.retrieved_time >= '2023-12-01 05:41:55.000000 +00:00') ps
              ON pc.session_id = ps.id
GROUP BY session_id, ps.player_uuid;


CREATE INDEX ix_tmp_play_session_sum_sess_id ON tmp_play_session_sum (session_id);
SET LOCK_TIMEOUT = 0;

UPDATE play_session pso
SET playtime_snapshot = dm.playtime_sum
FROM tmp_play_session_sum dm
WHERE pso.id = dm.session_id
  AND playtime_snapshot != dm.playtime_sum;

DROP INDEX IF EXISTS ix_tmp_play_session_sum_sess_id;
DROP TABLE IF EXISTS tmp_play_session_sum;
