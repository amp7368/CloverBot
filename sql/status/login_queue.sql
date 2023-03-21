SELECT COUNT(id) players_count, offline
FROM login_queue
GROUP BY offline
ORDER BY offline DESC