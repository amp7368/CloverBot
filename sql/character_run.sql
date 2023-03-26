SELECT player_uuid,
       c.type,
       d.name,
       d.runs_snapshot,
       d.runs_delta
FROM play_session s
         LEFT JOIN player_character c ON s.id = c.session_id
         LEFT JOIN raid_run d ON d.character_sku = c.sku
WHERE player_uuid IN ('bdd710fc-9c23-4ffd-a784-a511e746200e')
  AND d.runs_snapshot IS NOT NULL
  AND d.runs_delta NOT IN (0, d.runs_snapshot)

GROUP BY s.player_uuid,
         c.character_id,
         c.type,
         d.name,
         d.runs_snapshot,
         d.runs_delta
ORDER BY (MAX(d.runs_snapshot) - MIN(d.runs_snapshot)) DESC