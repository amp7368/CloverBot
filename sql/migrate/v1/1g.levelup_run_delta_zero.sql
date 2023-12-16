SET LOCK_TIMEOUT = 0;
DELETE
FROM levelup_run lr
WHERE lr.level_delta = 0;

SELECT lr.*, retrieved_time
FROM player_character pc
         LEFT JOIN play_session ps ON pc.session_id = ps.id
         RIGHT JOIN levelup_run lr ON lr.character_sku = pc.sku
WHERE character_id = '7822898d-be4d-4068-a89e-68052ad57a8e'
  AND name = 'fishing'
ORDER BY retrieved_time DESC;
