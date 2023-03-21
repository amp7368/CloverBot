SELECT player_uuid,
       DATE_TRUNC('HOUR', retrieved_time) AS retrieved_time,
       SUM(playtime_delta)                AS playtime_delta,
       SUM(combat_delta)                  AS combat_delta,
       SUM(items_identified_delta)        AS items_identified_delta,
       SUM(mobs_killed_delta)             AS mobs_killed_delta,
       SUM(prof_delta)                    AS total_prof_level_delta
FROM play_session s
WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195' --'bdd710fc-9c23-4ffd-a784-a511e746200e'
  AND s.retrieved_time BETWEEN '2023-03-20' AND '2023-04-01'
GROUP BY DATE_TRUNC('HOUR', retrieved_time), s.player_uuid;
SELECT DATE_TRUNC('hour', retrieved_time) AS retrieved_time,
       SUM(playtime_delta)                AS playtime_delta,
       SUM(combat_delta)                  AS combat_delta,
       SUM(items_identified_delta)        AS items_identified_delta,
       SUM(mobs_killed_delta)             AS mobs_killed_delta,
       SUM(prof_delta)                    AS total_prof_level_delta
FROM play_session s
WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195'
  AND retrieved_time BETWEEN date('2023-03-20') AND date('2023-04-01') + INTERVAL '1 hour'
GROUP BY DATE_TRUNC('hour', retrieved_time)