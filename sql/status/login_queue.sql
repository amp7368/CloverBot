SELECT COUNT(id) players_count, offline
FROM login_queue
GROUP BY offline
ORDER BY offline DESC;

SELECT ps.id,
       ps.join_time,
       ps.retrieved_time,
       ps.playtime_snapshot,
       ps.playtime_delta,
       ps.items_identified_snapshot,
       ps.items_identified_delta,
       ps.mobs_killed_snapshot,
       ps.mobs_killed_delta,
       ps.combat_snapshot,
       ps.combat_delta,
       ps.prof_snapshot,
       ps.prof_delta,
       ps.player_uuid,
       ps.guild_name
FROM play_session ps
WHERE (ps.player_uuid = 'fee05d22-06b2-4479-9ca5-6fd4db985227')
ORDER BY CASE
             WHEN ps.retrieved_time >= date('2023-04-03 15:51:32.0')
                 THEN ps.retrieved_time
             ELSE date('2023-04-05 01:00:00.0') END, ps.retrieved_time DESC
;
SELECT *
FROM play_session ps
WHERE (ps.player_uuid = 'fee05d22-06b2-4479-9ca5-6fd4db985227')

