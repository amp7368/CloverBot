SELECT DATE_TRUNC(:resolution, retrieved_time) AS retrieved,
       SUM(c.playtime_delta)                   AS playtime_delta,
       SUM(combat_delta)                       AS combat_delta,
       SUM(c.items_identified_delta)           AS items_identified_delta,
       SUM(c.mobs_killed_delta)                AS mobs_killed_delta,
       SUM(prof_delta)                         AS total_prof_level_delta
FROM play_session s
         LEFT JOIN player_character c ON s.id = c.session_id
WHERE player_uuid = :player
  AND retrieved_time BETWEEN date(:start) AND date(:end)
GROUP BY retrieved
ORDER BY retrieved;
