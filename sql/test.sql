SELECT guild.tag, COUNT(guild.name) row_count
FROM guild
WHERE guild.is_active IS TRUE
GROUP BY guild.tag
HAVING COUNT(guild.name) > 1;


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


SELECT *
FROM login_queue;

SELECT *
FROM blacklist;

select other.created, guild.*
from guild
         left join guild other
                   on guild.name = other.name
                       and guild.id != other.id
where guild.created > other.created
  and not guild.is_active
order by guild.name;


selecT last_status.id, notif.*
from (select distinct nth_value(id, 2)
                      over (partition by activity
                          order by end_at desc
                          ) id

      from service_status) last_status
         left join service_status_notification notif on notif.status_id = last_status.id;
-- left join service_status status on notif.status_id = last_status.id;

select activity, start_at, is_online, *
from service_status
order by end_at desc

delete
from play_session
where retrieved_time > now() - INTERVAL '2 hour';

select *
from play_session
order by retrieved_time desc;

select *
from blacklist;