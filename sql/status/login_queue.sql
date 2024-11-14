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
WHERE (ps.player_uuid = 'fee05d22-06b2-4479-9ca5-6fd4db985227');

SELECT id, COUNT(*)
FROM login_queue
GROUP BY id
HAVING COUNT(*) > 1;

SELECT COUNT(*), offline
FROM login_queue
GROUP BY offline
ORDER BY offline;

SELECT *
FROM login_queue
WHERE offline > 5;

SELECT *
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
WHERE ps.player_uuid = '42644be8-5c39-4566-8e74-0874bdcb5eb0'
  AND NOT EXISTS((
                 SELECT name
                 FROM levelup_run
                 WHERE pc.sku = character_sku))
ORDER BY retrieved_time DESC;


SELECT *
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
         LEFT JOIN levelup_run l ON pc.sku = l.character_sku
WHERE ps.player_uuid = '42644be8-5c39-4566-8e74-0874bdcb5eb0'
ORDER BY retrieved_time DESC;

SELECT *
FROM play_session
ORDER BY retrieved_time DESC
LIMIT 1000;


SELECT *
FROM player
WHERE username = 'Shadow_loll';

SELECT DISTINCT player_uuid
FROM clover.public.player_character pc
         LEFT JOIN public.play_session ps ON pc.session_id = ps.id
WHERE character_id IN (
                      SELECT DISTINCT pc2.character_id
                      FROM play_session ps2
                               LEFT JOIN player_character pc2 ON ps2.id = pc2.session_id
                      WHERE ps2.player_uuid = '5ff551f4-bd02-4f63-b7cf-71621f55ece9'
                      ORDER BY pc2.character_id)



