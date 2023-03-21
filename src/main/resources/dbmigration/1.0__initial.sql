-- apply changes
create table blacklist (
  failure                       integer not null,
  success                       integer not null,
  last_failure                  timestamptz,
  username                      varchar(255) not null,
  constraint pk_blacklist primary key (username)
);

create table player_character (
  sku                           uuid not null,
  character_id                  uuid,
  session_id                    uuid,
  items_identified_snapshot     integer,
  items_identified_delta        integer,
  mobs_killed_snapshot          integer,
  mobs_killed_delta             integer,
  blocks_walked_snapshot        bigint,
  blocks_walked_delta           bigint,
  logins_snapshot               integer,
  logins_delta                  integer,
  deaths_snapshot               integer,
  deaths_delta                  integer,
  playtime_snapshot             bigint,
  playtime_delta                bigint,
  type                          varchar(255),
  constraint pk_player_character primary key (sku)
);

create table dungeon_run (
  run_id                        uuid not null,
  character_sku                 uuid,
  runs_snapshot                 integer,
  runs_delta                    integer,
  name                          varchar(255),
  constraint pk_dungeon_run primary key (run_id)
);

create table guild (
  created                       timestamptz,
  name                          varchar(255) not null,
  tag                           varchar(255),
  constraint pk_guild primary key (name)
);

create table levelup_run (
  run_id                        uuid not null,
  character_sku                 uuid,
  level_snapshot                float,
  level_delta                   float,
  name                          varchar(255),
  constraint pk_levelup_run primary key (run_id)
);

create table login_queue (
  id                            uuid not null,
  join_time                     timestamptz not null,
  leave_time                    timestamptz,
  offline                       integer not null,
  is_online                     boolean default false not null,
  player                        varchar(255) not null,
  blacklist_username            varchar(255),
  constraint uq_login_queue_player unique (player),
  constraint uq_login_queue_blacklist_username unique (blacklist_username),
  constraint pk_login_queue primary key (id)
);

create table play_session (
  id                            uuid not null,
  player_uuid                   uuid,
  join_time                     timestamptz,
  retrieved_time                timestamptz,
  playtime_snapshot             bigint,
  playtime_delta                bigint,
  items_identified_snapshot     bigint,
  items_identified_delta        bigint,
  mobs_killed_snapshot          bigint,
  mobs_killed_delta             bigint,
  combat_snapshot               integer,
  combat_delta                  integer,
  prof_snapshot                 integer,
  prof_delta                    integer,
  guild_name                    varchar(255),
  constraint uq_play_session_player_uuid_join_time unique (player_uuid,join_time),
  constraint pk_play_session primary key (id)
);

create table player (
  uuid                          uuid not null,
  username                      varchar(255),
  constraint pk_player primary key (uuid)
);

create table raid_run (
  run_id                        uuid not null,
  character_sku                 uuid,
  runs_snapshot                 integer,
  runs_delta                    integer,
  name                          varchar(255),
  constraint pk_raid_run primary key (run_id)
);

-- foreign keys and indices
create index ix_player_character_session_id on player_character (session_id);
alter table player_character add constraint fk_player_character_session_id foreign key (session_id) references play_session (id) on delete restrict on update restrict;

create index ix_dungeon_run_character_sku on dungeon_run (character_sku);
alter table dungeon_run add constraint fk_dungeon_run_character_sku foreign key (character_sku) references player_character (sku) on delete restrict on update restrict;

create index ix_levelup_run_character_sku on levelup_run (character_sku);
alter table levelup_run add constraint fk_levelup_run_character_sku foreign key (character_sku) references player_character (sku) on delete restrict on update restrict;

alter table login_queue add constraint fk_login_queue_blacklist_username foreign key (blacklist_username) references blacklist (username) on delete restrict on update restrict;

create index ix_play_session_player_uuid on play_session (player_uuid);
alter table play_session add constraint fk_play_session_player_uuid foreign key (player_uuid) references player (uuid) on delete restrict on update restrict;

create index ix_play_session_guild_name on play_session (guild_name);
alter table play_session add constraint fk_play_session_guild_name foreign key (guild_name) references guild (name) on delete restrict on update restrict;

create index ix_raid_run_character_sku on raid_run (character_sku);
alter table raid_run add constraint fk_raid_run_character_sku foreign key (character_sku) references player_character (sku) on delete restrict on update restrict;

create index if not exists ix_player_character_character_id_session_id on player_character (character_id,session_id);
create index if not exists ix_dungeon_run_character_sku_name on dungeon_run (character_sku,name);
create index if not exists ix_levelup_run_character_sku_name on levelup_run (character_sku,name);
create index if not exists ix_play_session_player_uuid_join_time on play_session (player_uuid,join_time);
create index if not exists ix_raid_run_character_sku_name on raid_run (character_sku,name);
