SELECT MIN(retrieved_time) - INTERVAL '1 second'
FROM player_character pc
         LEFT JOIN play_session ps ON pc.session_id = ps.id
         LEFT JOIN player ON ps.player_uuid = player.uuid
WHERE pc.playtime_delta < 0
  AND retrieved_time BETWEEN '2023-11-30' AND '2023-12-05'; -- MIGRATION HAPPENED RIGHT AFTER 2023-12-01 05:41:54.000000 +00:00


SET LOCK_TIMEOUT = 0;
UPDATE play_session ps
SET playtime_snapshot = playtime_snapshot * 5,
    playtime_delta    = playtime_delta * 5,
    api_version_id    = 1
WHERE retrieved_time < '2023-12-01 05:41:54.000000 +00:00';

UPDATE api_version
SET name = 'v3.2-alpha'
WHERE name = 'v3.2';

SELECT pc.sku
INTO TABLE players_before_v3
FROM play_session ps
         RIGHT JOIN player_character pc ON ps.id = pc.session_id
WHERE ps.retrieved_time < '2023-12-01 05:41:54.000000 +00:00';

DROP TABLE IF EXISTS delete_me;
DROP INDEX IF EXISTS delete_me_sku_index;

CREATE INDEX ix_players_before_v3_sku ON players_before_v3 (sku);

-- compare before and after next change
-- This play_session is on Nov 23
-- 101,16
-- 22,0
-- 0,0
-- 1,0
SELECT *
FROM player_character
WHERE session_id = 'fd599362-1803-490d-b68b-1b70db667fa4';

EXPLAIN
UPDATE player_character
SET playtime_snapshot = pc.playtime_snapshot * 5,
    playtime_delta    = pc.playtime_delta * 5
FROM player_character pc
         INNER JOIN players_before_v3 dm ON pc.sku = dm.sku
WHERE player_character.sku = pc.sku;

DROP TABLE tmp_tbl;
DROP TABLE players_before_v3;
DROP INDEX ix_players_before_v3_sku;