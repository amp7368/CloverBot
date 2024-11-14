SELECT guild.tag, COUNT(guild.name) row_count
FROM guild
WHERE guild.is_active IS TRUE
GROUP BY guild.tag
HAVING COUNT(guild.name) > 1;

SELECT *
FROM guild g
WHERE g.name = 'Anomalia';
SELECT *
FROM guild g1
         LEFT JOIN guild g2 ON g1.name = g2.name
WHERE g1.id != g2.id
  AND g1.is_active IS TRUE
  AND g2.is_active IS TRUE;

UPDATE play_session ps
SET guild_id = (SELECT DISTINCT g2.id
                FROM guild g1
                         LEFT JOIN guild g2 ON g1.name = g2.name
                WHERE g1.id = ps.guild_id
                  AND g2.is_active)
WHERE ps.retrieved_time >= NOW() - INTERVAL '72 hours';



SELECT COUNT(*)
FROM (SELECT DISTINCT ps.guild_id               old_guild_id,
                      g1.name                   old_guild_name,
                      new_guild.id,
                      new_guild.name,
                      (SELECT COUNT(*)
                       FROM guild gc
                       WHERE gc.name = g1.name) guild_count
      FROM play_session ps
               LEFT JOIN guild g1 ON g1.id = ps.guild_id
               LEFT JOIN guild g2 ON g1.name = g2.name
               LEFT JOIN guild new_guild ON new_guild.id = COALESCE(g2.id, ps.guild_id)
      WHERE g1.id = ps.guild_id
        AND COALESCE(g2.id, ps.guild_id) != ps.guild_id
        AND g2.is_active
      ORDER BY guild_count DESC) abc;

DELETE
FROM guild g
WHERE NOT g.is_active
  AND NOT EXISTS(SELECT id
                 FROM play_session ps
                 WHERE ps.guild_id = g.id);

SELECT DISTINCT guild_id, g1.name, MIN(ps.retrieved_time)
FROM play_session ps
         LEFT JOIN guild g1 ON g1.id = ps.guild_id
         LEFT JOIN guild g2 ON g1.name = g2.name AND g1.id != g2.id
    AND g2.is_active
-- WHERE ps.retrieved_time >= NOW() - INTERVAL '72 hours'
GROUP BY guild_id, g1.name
ORDER BY MIN(ps.retrieved_time) DESC;



SELECT *
FROM guild
WHERE name = 'Yahyas Spoons';

DELETE
FROM guild g
WHERE id IN (SELECT DISTINCT g2.id
             FROM guild g1
                      LEFT JOIN guild g2 ON g1.name = g2.name
             WHERE g2.id IS NOT NULL
               AND g2.is_active);


SELECT *
FROM player p
         LEFT JOIN play_session ps ON p.uuid = ps.player_uuid
WHERE username LIKE 'ThEpicFerret';

SELECT *
FROM guild
WHERE name = 'Cult of Mages';

SELECT COUNT(*)
FROM guild;


SELECT *
FROM login_queue
WHERE player = '779de1cb-7bec-42c2-8128-e3118289b4a9';

SELECT *
FROM login_queue;

SELECT *
FROM play_session
ORDER BY retrieved_time DESC
LIMIT 1000;

SELECT *
FROM blacklist;

SELECT *
FROM guild
WHERE tag IS NULL;

SELECT *
FROM play_session
         LEFT JOIN guild ON guild.id = '0e4918a0-6767-4d9f-841e-26a933539662';


select *
from login_queue
order by join_time desc;
select *
from player
where uuid = uuid_in('3e9a083fc2ed4b56a8e6f98ad43b12a3');

select *
from player
where username ilike '%nyxx%';

select *
from command_log;

select *
from play_session
order by retrieved_time desc
limit 5;

select *
from guild
where tag = 'Aeq';

select other.created, guild.*
from guild
         left join guild other
                   on guild.name = other.name
                       and guild.id != other.id
where guild.created > other.created
  and not guild.is_active
order by guild.name;

select *
from guild;



