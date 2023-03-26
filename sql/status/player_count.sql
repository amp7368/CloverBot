SELECT player_count,
       ('{Mon,Tue,Wed,Thu,Fri,Sat,Sun}'::TEXT[])[EXTRACT(ISODOW FROM retrieved)] day_of_week,
       retrieved
FROM (
     SELECT DATE_TRUNC('DAY', retrieved_time) retrieved,
            COUNT(DISTINCT player_uuid)       player_count
     FROM play_session ps
     GROUP BY retrieved) q;