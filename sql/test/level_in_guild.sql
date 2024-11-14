SELECT DISTINCT (player.uuid),
                FIRST_VALUE(max_level)
                OVER (PARTITION BY player.uuid ORDER BY max_level DESC ) AS max_level,
                player.username,
                FIRST_VALUE((
                            SELECT pc4.type
                            FROM player_character pc4
                            WHERE q.character_id = pc4.character_id
                            LIMIT 1)) OVER (ORDER BY max_level)             class_type
FROM (
     SELECT ROUND(MAX(level_snapshot)) max_level,
            character_id,
            (
            SELECT player_uuid
            FROM play_session ps2
                     LEFT JOIN player_character pc2 ON ps2.id = pc2.session_id
            WHERE pc2.character_id = pc.character_id
            LIMIT 1)                   player_uuid
     FROM levelup_run l
              LEFT JOIN player_character pc ON l.character_sku = pc.sku
     WHERE character_id IN (
                           SELECT DISTINCT pc.character_id
                           FROM player_character pc
                                    LEFT JOIN play_session ps ON pc.session_id = ps.id
                           WHERE ps.player_uuid IN ('33b6a693-db57-4b3f-b6df-d0772876994f',
                                                    '346b8f2b-d672-43ec-8c7f-f960125df1da',
                                                    'e9b2a02b-f07c-42fb-aa0a-210834dc1e6e',
                                                    '4db3086b-b4dc-47f5-b5b6-12e3ee752d83',
                                                    '20a03156-e447-4209-9539-c4680101179c',
                                                    'c75c9c45-4a36-4fc8-a4d0-fce922997494',
                                                    '55309fc6-413c-4e05-8394-52f28ae41909',
                                                    '0edc3eb6-74d8-49b6-8b2a-3c0782183e3a',
                                                    '084e52ff-bffa-4af7-b1c7-e401039b32f2',
                                                    '5be48ae3-82f4-4f4f-baf1-c26fcbe1788a',
                                                    '7c76f57c-258d-4b27-b943-a215bc23f204',
                                                    'a6cd1eb9-cbd4-4537-906c-c8c48a64455b',
                                                    '2247642b-a2b7-4f80-900b-711b2a54430a',
                                                    '3f275026-3fc3-4ae9-9d47-45f584d11d57',
                                                    'd0b2822a-d968-4958-af73-5ec1342a9c80',
                                                    '8d6c75ad-9044-454d-ada7-bb79b47a734f',
                                                    '1531fd35-397c-4f44-8fa0-ae638b849a1a',
                                                    '9b8b2f75-33c2-42f4-9a0e-80735a9fdd72',
                                                    '99834bf2-035f-452e-9a38-ada2e7dd5e62',
                                                    '029457dd-bbc3-489c-8af9-f303b7818c05',
                                                    'be1fd2a6-8aac-480b-8333-c7121fefb74a',
                                                    '50cf70d4-f901-403b-a424-99d8a287ba2e',
                                                    'd3c4342b-5759-4139-a18a-856bd72ca281',
                                                    'fc86c444-93ca-452c-8d0f-11be4a7cf00c',
                                                    'cccb2e50-c714-4e21-86c4-cba01d9da4cb',
                                                    '7a8e93e9-07e9-4afc-8831-38f4a462157c',
                                                    '18e66c93-0304-424e-9dcc-5d6969e1f182',
                                                    'ca4fa31e-d2db-42d0-ba40-c052db10eaa5',
                                                    '4b5d92b3-2c4b-4701-ad73-63fb79c51a10',
                                                    '5bff02a5-99b7-4327-bc34-d1a77fb2dbbb',
                                                    'f0baa839-3d53-4a58-a438-73f5f1b88525',
                                                    '3cd8f827-f1b2-4cb4-8723-6f32539dfa98',
                                                    'bf129d41-ca83-4e5b-9594-8e106584cd59',
                                                    'ecbc77f4-0f18-4ec2-9653-ce51e560a4eb',
                                                    '25b07811-479b-44e1-b88c-a9f527bbcbe4',
                                                    '35122d14-4bf3-458d-a563-3c0d2f106e75',
                                                    '58dd21a8-496e-4fc5-9201-6adb433a5d41',
                                                    'a11327d7-3042-4d35-a1c1-07c27e23bb3b',
                                                    'acbfa24c-7775-4aea-aa1e-a2a5aea18066',
                                                    'fc41efa1-c9b2-481b-b223-b29c6d9316be',
                                                    '9e98db92-5659-4b3a-b1ed-8efbcbafbcfc',
                                                    'c7eb9263-36c0-48fc-b7e4-cd3da3ad231e',
                                                    'f43954c6-1829-46f4-b3fd-74bab5096f4f',
                                                    'd2559107-4bbf-4d17-9915-3c3611c14a23',
                                                    '2af9e14f-cbc2-4dd8-8eef-8dd9e67cccfa',
                                                    '8696a15b-fedf-42f4-b1aa-22d8d2157063',
                                                    'f8be8800-d189-4221-829c-f29478678cb4',
                                                    '322896d3-f061-450b-85b6-95bda26fe5d7',
                                                    '22482784-d726-44aa-8e9e-e9dfbf97384f',
                                                    '6bbbba72-110f-44e0-a730-2faab1d21531',
                                                    '948a8809-f7ea-4944-99f2-608086eba4ef'))
       AND l.name = 'combat'
     GROUP BY character_id) q
         LEFT JOIN player ON player.uuid = q.player_uuid
ORDER BY max_level DESC;



SELECT lr.*, pc.type
FROM levelup_run lr
         LEFT JOIN player_character pc ON lr.character_sku = pc.sku
         LEFT JOIN play_session ps ON pc.session_id = ps.id
WHERE player_uuid = '9c1d20f8-5873-49bb-94a0-d997de34d7d5'
  AND lr.name = 'combat'
ORDER BY retrieved_time DESC
;

SELECT (
       SELECT play_session.retrieved_time
       FROM play_session
       WHERE play_session.id = session_id) time,
       level_snapshot,
       level_delta
FROM levelup_run
         LEFT JOIN player_character ON levelup_run.character_sku = player_character.sku
WHERE character_id = '9fbd4495-a2c5-4563-84f0-785c61a6ec95'
  AND levelup_run.name = 'combat'
ORDER BY (
         SELECT play_session.retrieved_time
         FROM play_session
         WHERE play_session.id = session_id) DESC

