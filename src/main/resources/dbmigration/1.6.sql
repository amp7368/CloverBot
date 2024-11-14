-- apply changes
CREATE TABLE api_version
(
    id   SMALLINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(20),
    CONSTRAINT uq_api_version_name UNIQUE (name),
    CONSTRAINT pk_api_version PRIMARY KEY (id)
);

-- apply alter tables
ALTER TABLE play_session
    ADD COLUMN api_version_id SMALLINT;
-- foreign keys and indices
CREATE INDEX ix_play_session_api_version_id ON play_session (api_version_id);
ALTER TABLE play_session
    ADD CONSTRAINT fk_play_session_api_version_id FOREIGN KEY (api_version_id) REFERENCES api_version (id) ON DELETE RESTRICT ON UPDATE RESTRICT;

-- Insert first entities
INSERT INTO api_version
VALUES (0, 'unknown');
INSERT INTO api_version
VALUES (1, 'v2.0');
INSERT INTO api_version
VALUES (2, 'v3.1');
INSERT INTO api_version
VALUES (3, 'v3.2-alpha');
