SELECT lr.*
FROM play_session ps
         LEFT JOIN (
                   SELECT *
                   FROM player_character
                   WHERE character_id = :character_id) pc ON ps.id = pc.session_id
         LEFT JOIN (
                   SELECT *
                   FROM levelup_run
                   WHERE name = :level_name) lr ON lr.character_sku = pc.sku
WHERE lr.run_id IS NOT NULL
ORDER BY retrieved_time DESC
LIMIT 1;
-- bind
-- :character_id='d8a8b846-75cf-48ca-b5eb-d89cb7fdddb0'
-- :level_name='combat'