SELECT pc.sku
INTO TABLE players_at_v3_2
FROM play_session ps
         RIGHT JOIN player_character pc ON ps.id = pc.session_id
WHERE ps.retrieved_time > '2023-12-05 09:35:47.000000 +00:00';

CREATE INDEX ix_players_at_v3_2 ON players_at_v3_2 (sku);

EXPLAIN
UPDATE player_character
SET playtime_snapshot = pc.playtime_snapshot * 60,
    playtime_delta    = pc.playtime_delta * 60
FROM player_character pc
         INNER JOIN players_at_v3_2 dm ON pc.sku = dm.sku
WHERE player_character.sku = pc.sku;

UPDATE play_session ps
SET playtime_snapshot = playtime_delta * 60,
    playtime_delta    = playtime_delta * 60,
    api_version_id    = 3
WHERE ps.retrieved_time > '2023-12-05 09:35:47.000000 +00:00';

