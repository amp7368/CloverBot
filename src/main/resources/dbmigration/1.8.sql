-- apply changes
create table command_log (
  id                            uuid not null,
  created_at                    timestamptz not null,
  sender_discord_id             bigint not null,
  channel_id                    bigint not null,
  server_id                     bigint,
  command_message               varchar(255) not null,
  sender_discord_name           varchar(255) not null,
  sender_effective_name         varchar(255) not null,
  sender_avatar_url             varchar(255) not null,
  channel_name                  varchar(255) not null,
  channel_type                  varchar(255) not null,
  server_name                   varchar(255),
  constraint pk_command_log primary key (id)
);

