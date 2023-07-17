-- apply alter tables
ALTER TABLE guild
    ADD COLUMN id UUID;
ALTER TABLE guild
    ADD COLUMN is_active BOOLEAN DEFAULT TRUE NOT NULL;
ALTER TABLE play_session
    ADD COLUMN guild_id UUID;
-- apply post alter
ALTER TABLE auth_role_permission_bridge
    ADD CONSTRAINT uq_auth_role_permission_bridge_role_permission UNIQUE (role_id, permission_id);
-- foreign keys and indices
CREATE INDEX ix_play_session_guild_id ON play_session (guild_id);
ALTER TABLE play_session
    ADD CONSTRAINT fk_play_session_guild_id FOREIGN KEY (guild_id) REFERENCES guild (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Set random id's
UPDATE guild
SET id = gen_random_uuid();
