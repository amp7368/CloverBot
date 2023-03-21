SELECT DISTINCT character_id
FROM play_session s
         LEFT JOIN player_character pc ON s.id = pc.session_id
WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195';
