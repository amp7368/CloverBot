SELECT DISTINCT rr.name
FROM raid_run rr;

SELECT DISTINCT character_id
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
WHERE ps.player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195';


SELECT ps.retrieved_time AS retrieved,
       rr.runs_snapshot,
       rr.runs_delta,
       rr.name           AS raid,
       pc.character_id
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
         LEFT JOIN raid_run rr ON pc.sku = rr.character_sku
WHERE rr.name = 'Nest of the Grootslangs'
  AND pc.character_id = '9e85fb33-d603-454e-b41b-ef974199626e'
  AND retrieved_time < date('2023-04-01')
ORDER BY CASE
             WHEN ps.retrieved_time >= date('2023-03-23')
                 THEN ps.retrieved_time
             ELSE date('3000-03-23') END
       , ps.retrieved_time DESC
LIMIT 1;


SELECT ps.retrieved_time AS retrieved,
       rr.runs_snapshot,
       rr.runs_delta,
       rr.name           AS raid,
       pc.character_id
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
         LEFT JOIN raid_run rr ON pc.sku = rr.character_sku
WHERE rr.name = 'Nest of the Grootslangs'
  AND pc.character_id = '9e85fb33-d603-454e-b41b-ef974199626e'
  AND ps.retrieved_time < date('2023-04-01')
ORDER BY ps.retrieved_time DESC
LIMIT 1;

