SELECT rr.*, session_id, ps.retrieved_time
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
         LEFT JOIN raid_run rr ON pc.sku = rr.character_sku
WHERE rr.name = 'Nest of the Grootslangs'
  AND pc.character_id = '9e85fb33-d603-454e-b41b-ef974199626e'
ORDER BY ps.retrieved_time DESC
LIMIT 1;

