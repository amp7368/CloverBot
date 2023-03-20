SELECT PLAYER_UUID,
       C.TYPE,
       D.NAME,
       d.runssnapshot,
       d.runsdelta
FROM PLAY_SESSION S
         LEFT JOIN PLAYER_CHARACTER C ON S.ID = C.SESSION_ID
         LEFT JOIN raid_run D ON D.CHARACTER_ID = C.ID
WHERE player_uuid in ('bdd710fc-9c23-4ffd-a784-a511e746200e')
  AND D.RUNSSNAPSHOT IS NOT NULL
  and D.RUNSDELTA not in (0, d.runssnapshot)

GROUP BY S.PLAYER_UUID,
         C.character_id,
         C.type,
         D.NAME,
         d.runssnapshot,
         d.runsdelta
ORDER BY (MAX(D.RUNSSNAPSHOT) - MIN(D.RUNSSNAPSHOT)) DESC