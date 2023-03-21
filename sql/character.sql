SELECT s.player_uuid,
       pc.*,
       DATE_TRUNC('HOUR', s.retrieved_time) AS time
FROM play_session s
         LEFT JOIN player_character pc ON s.id = pc.session_id

WHERE player_uuid = 'd9d489fd-96ec-4831-8fd1-9e131d02c392' --'bdd710fc-9c23-4ffd-a784-a511e746200e'
