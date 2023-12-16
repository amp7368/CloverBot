SET LOCK_TIMEOUT = 0;

SELECT pc.sku
INTO TABLE players_at_v3_1
FROM play_session ps
         RIGHT JOIN player_character pc ON ps.id = pc.session_id
WHERE ps.retrieved_time BETWEEN '2023-12-01 05:41:54.000000 +00:00' AND '2023-12-05 09:35:47.000000 +00:00';

CREATE INDEX ix_players_at_v3_1 ON players_at_v3_1 (sku);

SET LOCK_TIMEOUT = 0;
EXPLAIN
UPDATE player_character pco
SET playtime_snapshot = TRUNC(pco.playtime_snapshot * 60 * 5 / 4.7),
    playtime_delta    = TRUNC(pco.playtime_delta * 60 * 5 / 4.7)
FROM player_character pc
         INNER JOIN players_at_v3_1 dm ON pc.sku = dm.sku
WHERE pco.sku = pc.sku;


UPDATE play_session ps
SET playtime_snapshot = TRUNC(playtime_delta * 60 * 5 / 4.7),
    playtime_delta    = TRUNC(playtime_delta * 60 * 5 / 4.7),
    api_version_id    = 2
WHERE ps.retrieved_time BETWEEN '2023-12-01 05:41:54.000000 +00:00' AND '2023-12-05 09:35:47.000000 +00:00';

DROP INDEX ix_players_at_v3_1;
DROP TABLE players_at_v3_1;

SELECT DISTINCT api_version_id
FROM play_session
