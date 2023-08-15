SELECT guild.tag, COUNT(guild.name) row_count
FROM guild
GROUP BY guild.tag
HAVING COUNT(guild.name) > 1;

SELECT *
FROM guild
WHERE tag = 'Dawn'