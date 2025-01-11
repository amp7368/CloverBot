SELECT rr.runs_delta, name, ps.player_uuid, ps.retrieved_time
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
         LEFT JOIN public.raid_run rr ON pc.sku = rr.character_sku
WHERE player_uuid = 'b0045e0e-bb24-424b-b6d0-a64e1d7a73a1'
  AND rr.runs_delta IS NOT NULL
  AND rr.runs_delta != 0
ORDER BY retrieved_time DESC;


SELECT rr.runs_delta, name, ps.player_uuid, ps.retrieved_time
FROM play_session ps
         LEFT JOIN player_character pc ON ps.id = pc.session_id
         LEFT JOIN public.raid_run rr ON pc.sku = rr.character_sku
WHERE player_uuid = 'b0045e0e-bb24-424b-b6d0-a64e1d7a73a1'
  AND rr.runs_delta IS NOT NULL
  AND rr.runs_delta != 0
ORDER BY retrieved_time DESC;



