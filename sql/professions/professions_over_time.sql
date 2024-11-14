SELECT SUM(lr.level_snapshot) snapshot,
       SUM(lr.level_delta)    delta,
       lr.name,
       ps.retrieved_time
FROM levelup_run lr
         LEFT JOIN player_character pc ON lr.character_sku = pc.sku
         LEFT JOIN play_session ps ON pc.session_id = ps.id
WHERE lr.character_sku IN (
                          SELECT pc.sku
                          FROM player_character pc
                                   LEFT JOIN play_session ps ON pc.session_id = ps.id
                          WHERE ps.player_uuid = (
                                                 SELECT uuid
                                                 FROM player
                                                 WHERE username LIKE 'TheSadJester'))
GROUP BY lr.name, ps.retrieved_time
ORDER BY lr.name, ps.retrieved_time
;
