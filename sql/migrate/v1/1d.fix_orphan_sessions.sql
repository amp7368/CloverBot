DELETE
FROM play_session ps
WHERE NOT EXISTS(
                SELECT *
                FROM player_character pc
                WHERE pc.session_id = ps.id);

SELECT *
FROM play_session
WHERE player_uuid = 'e28eee6d-be90-4618-b773-ca66673cadce';