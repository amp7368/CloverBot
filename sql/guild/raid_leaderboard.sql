select rr.runs_delta, name, ps.retrieved_time
from play_session ps
         left join player_character pc on ps.id = pc.session_id
         left join public.raid_run rr on pc.sku = rr.character_sku
where player_uuid = 'b0045e0e-bb24-424b-b6d0-a64e1d7a73a1'
  and rr.runs_delta is not null
  and rr.runs_delta != 0
order by retrieved_time desc;

select count(ps.id), player_uuid
from play_session ps
group by player_uuid
order by count(ps.id) desc

select sender_discord_name, count(*)
from command_log
group by sender_discord_name
order by count(*) desc;

