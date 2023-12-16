-- drop dependencies
DROP INDEX IF EXISTS ix_player_character_character_id_session_id;
DROP INDEX IF EXISTS ix_play_session_player_uuid_join_time;
-- foreign keys and indices
CREATE INDEX IF NOT EXISTS ix_player_character_character_id ON player_character (character_id);
CREATE INDEX IF NOT EXISTS ix_play_session_retrieved_time ON play_session (retrieved_time);

-- drop old index
DROP INDEX IF EXISTS ix_play_session_guild_name;
