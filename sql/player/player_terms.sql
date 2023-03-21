SELECT DATE_TRUNC('HOUR', retrieved_time) AS retrieved,
       SUM(playtime_delta)                AS playtime_delta,
       SUM(combat_delta)                  AS combat_delta,
       SUM(items_identified_delta)        AS items_identified_delta,
       SUM(mobs_killed_delta)             AS mobs_killed_delta,
       SUM(prof_delta)                    AS total_prof_level_delta
FROM play_session s
WHERE player_uuid = '56ebccd5-c270-49a5-9659-460c82e0b195'
  AND retrieved_time BETWEEN date('2023-03-20') AND date('2023-04-01')
GROUP BY retrieved