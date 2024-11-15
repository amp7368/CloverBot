-- drop dependencies
alter table play_session
    drop constraint uq_play_session_player_uuid_join_time;
-- apply changes
create table service_status
(
    id          uuid                  not null,
    is_online   boolean default false not null,
    start_at    timestamptz           not null,
    end_at      timestamptz           not null,
    previous_id uuid,
    activity    varchar(12)           not null,
    constraint ck_service_status_activity check ( activity in ('PROGRAM', 'DISCORD_BOT', 'PLAY_SESSION')),
    constraint uq_service_status_previous_id unique (previous_id),
    constraint pk_service_status primary key (id)
);

create table service_status_notification
(
    id            uuid        not null,
    created_at    timestamptz not null,
    status_id     uuid,
    message_id    bigint      not null,
    channel_id    bigint      not null,
    type          varchar(6)  not null,
    success       varchar(7)  not null,
    error_message varchar(255),
    constraint ck_service_status_notification_type check ( type in ('REPORT', 'PING')),
    constraint ck_service_status_notification_success check ( success in ('PENDING', 'FAILED', 'RESENT', 'SENT')),
    constraint pk_service_status_notification primary key (id)
);

-- foreign keys and indices
alter table service_status
    add constraint fk_service_status_previous_id foreign key (previous_id) references service_status (id) on delete restrict on update restrict;

create index ix_service_status_notification_status_id on service_status_notification (status_id);
alter table service_status_notification
    add constraint fk_service_status_notification_status_id foreign key (status_id) references service_status (id) on delete restrict on update restrict;

create index if not exists ix_service_status_activity on service_status (activity);
create index if not exists ix_service_status_start_at on service_status (start_at);
