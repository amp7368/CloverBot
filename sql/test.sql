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

SELECT other.created, guild.*
FROM guild
         LEFT JOIN guild other
                   ON guild.name = other.name
                       AND guild.id != other.id
WHERE guild.created > other.created
  AND NOT guild.is_active
ORDER BY guild.name;


SELECT last_status.id, notif.*
FROM (SELECT DISTINCT NTH_VALUE(id, 2)
                      OVER (PARTITION BY activity
                          ORDER BY end_at DESC
                          ) id

      FROM service_status) last_status
         LEFT JOIN service_status_notification notif ON notif.status_id = last_status.id;
-- left join service_status status on notif.status_id = last_status.id;

SELECT activity, start_at, is_online, *
FROM service_status
ORDER BY end_at DESC;

SELECT *
FROM service_status_notification

SELECT *
FROM play_session
ORDER BY retrieved_time DESC;

