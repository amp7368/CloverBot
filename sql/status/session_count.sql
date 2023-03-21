SELECT COUNT(id) session_count, MAX(retrieved_time) latest, player_uuid
FROM play_session
GROUP BY player_uuid
ORDER BY session_count DESC