SELECT r.*
FROM (
     SELECT type,
            DATE_TRUNC('HOUR', retrieved_time) AS retrieved,
            SUM(pc.playtime_delta)             AS playtime_delta,
            SUM(pc.items_identified_delta)     AS items_identified_delta,
            SUM(pc.mobs_killed_delta)          AS mobs_killed_delta,
            SUM(pc.blocks_walked_delta)        AS blocks_walked_delta,
            SUM(pc.logins_delta)               AS logins_delta,
            SUM(pc.deaths_delta)               AS deaths_delta
     FROM player_character pc
              LEFT JOIN play_session s
                        ON pc.session_id = s.id
     WHERE pc.character_id = '073b9d5e-8413-4e8f-aed7-448ceeed8f11'
       AND s.retrieved_time BETWEEN date('2023-03-20') AND date('2023-04-01')
     GROUP BY retrieved, type
     ORDER BY retrieved) r
WHERE playtime_delta != 0
   OR items_identified_delta != 0
   OR mobs_killed_delta != 0
   OR blocks_walked_delta != 0
   OR logins_delta != 0
   OR deaths_delta != 0;