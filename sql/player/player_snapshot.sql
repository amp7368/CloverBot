SELECT *
FROM play_session s
WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195'
  AND retrieved_time BETWEEN '2023-03-20' AND date('2023-04-01')
ORDER BY retrieved_time DESC
LIMIT 1;
SELECT *
FROM play_session s
WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195'
  AND retrieved_time BETWEEN '2023-03-20' AND date('2023-04-01')
ORDER BY retrieved_time
LIMIT 1;
