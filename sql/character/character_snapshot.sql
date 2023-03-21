SELECT pc.*
FROM player_character pc
         LEFT JOIN play_session s ON pc.session_id = s.id
WHERE pc.character_id = '073b9d5e-8413-4e8f-aed7-448ceeed8f11'
  AND s.retrieved_time BETWEEN date('2023-03-20') AND date('2023-04-01')
ORDER BY retrieved_time DESC
LIMIT 1;
