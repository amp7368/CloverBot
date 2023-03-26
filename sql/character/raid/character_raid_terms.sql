SELECT DATE_TRUNC('HOUR', retrieved_time) AS retrieved,
       SUM(rr.runs_delta)                 AS runs_delta,
       rr.name                            AS raid,
       pc.character_id
FROM raid_run rr
         LEFT JOIN player_character pc ON pc.sku = rr.character_sku
         LEFT JOIN play_session s ON pc.session_id = s.id
WHERE pc.character_id
    IN (
       SELECT DISTINCT character_id
       FROM play_session s
                LEFT JOIN player_character pc ON s.id = pc.session_id
       WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195')
  AND s.retrieved_time BETWEEN date('2023-03-20') AND date('2023-04-01')
  AND rr.runs_delta != 0
GROUP BY retrieved, rr.name, pc.type, pc.character_id
ORDER BY retrieved;


