SELECT player_count,
       ('{Mon,Tue,Wed,Thu,Fri,Sat,Sun}'::TEXT[])[EXTRACT(ISODOW FROM retrieved)] day_of_week,
       retrieved
FROM (SELECT DATE_TRUNC('DAY', retrieved_time) retrieved,
             COUNT(DISTINCT player_uuid)       player_count
      FROM play_session ps
      GROUP BY retrieved) q
ORDER BY retrieved DESC;


select ps2.id
into tmp_tbl
from play_session ps2
         join (select ps1.player_uuid, min(ps1.join_time) join_time
               from play_session ps1
               where ps1.retrieved_time is not null
               group by ps1.player_uuid) ps1
              on ps1.player_uuid = ps2.player_uuid and
                 timestamptz_eq(ps1.join_time, ps2.join_time)

SELECT player_playtime,
       ('{Mon,Tue,Wed,Thu,Fri,Sat,Sun}'::TEXT[])[EXTRACT(ISODOW FROM retrieved)] day_of_week,
       retrieved
FROM (SELECT DATE_TRUNC('DAY', retrieved_time) retrieved,
             SUM(pc.playtime_delta) * 4.7 / 60 player_playtime
      FROM (select *
            from player_character pc
            where not exists  (select 1 from tmp_tbl where tmp_tbl.id = pc.session_id)) pc
               left join play_session ps on pc.session_id = ps.id
      GROUP BY retrieved) q
ORDER BY retrieved;

SELECT *
FROM play_session ps
WHERE ps.player_uuid = '2ecb1026-9fb2-4f6c-b37b-e4999390f7b9';


