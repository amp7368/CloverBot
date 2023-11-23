UPDATE guild
SET is_active = TRUE

FROM guild g1
         LEFT JOIN guild g2 ON g1.name = g2.name
WHERE g1.id != g2.id
  AND g1.created IS NULL
  AND g1.is_active;

SELECT *
FROM play_session
WHERE guild_id IN (
                  SELECT g1.id
                  FROM guild g1
                           LEFT JOIN guild g2 ON g1.name = g2.name
                  WHERE g1.id != g2.id
                    AND g1.created IS NULL
                    AND g1.is_active)
ORDER BY retrieved_time;


UPDATE play_session ps
SET guild_id = (
               SELECT id
               FROM guild
               WHERE name = (
                            SELECT name
                            FROM guild
                            WHERE guild.id = ps.guild_id)
               ORDER BY NOT guild.is_active
               LIMIT 1)
WHERE guild_id IS NOT NULL;

