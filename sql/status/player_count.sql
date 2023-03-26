SELECT player_count,
       ('{Sun,Mon,Tue,Wed,Thu,Fri,Sat}'::TEXT[])[EXTRACT(ISODOW FROM retrieved) + 1] day_of_week,
       retrieved
FROM (
     SELECT DATE_TRUNC('DAY', retrieved_time) retrieved,
            COUNT(DISTINCT player_uuid)       player_count
     FROM play_session ps
     GROUP BY retrieved) q;