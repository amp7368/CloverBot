SELECT s.player_uuid
     , s.*
     , DATE_TRUNC('HOUR',
                  s.RETRIEVED_TIME) AS time
FROM player_character pc
         LEFT JOIN play_session s on s.id = pc.session_id

WHERE player_uuid = 'bdd710fc-9c23-4ffd-a784-a511e746200e'
