SELECT last_guild, current_guild, retrieved_time
FROM (
     SELECT LAG(guild_id, 1)
            OVER (ORDER BY retrieved_time) last_guild,
            guild_id                       current_guild,
            retrieved_time
     FROM play_session
     WHERE player_uuid = '6c61961a-3b92-4a17-bf4e-6ffaa85b21b5'
     ORDER BY retrieved_time) sess
WHERE current_guild IS DISTINCT FROM last_guild; -- bind:player='6c61961a-3b92-4a17-bf4e-6ffaa85b21b5'

SELECT retrieved_time, guild_name
FROM play_session ps
WHERE player_uuid = '6c61961a-3b92-4a17-bf4e-6ffaa85b21b5'
ORDER BY retrieved_time;
