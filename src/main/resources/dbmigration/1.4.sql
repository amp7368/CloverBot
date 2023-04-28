-- drop dependencies
alter table if exists didentity_role_bridge drop constraint if exists fk_didentity_role_bridge_identity_id;
alter table if exists drole_permission_bridge drop constraint if exists fk_drole_permission_bridge_role_id;
-- apply changes
create table auth_identity_role_bridge (
  identity_id                   uuid,
  role_id                       uuid,
  constraint uq_auth_identity_role_bridge_role_id unique (role_id)
);

create table auth_role_permission_bridge (
  role_id                       uuid not null,
  permission_id                 uuid not null,
  constraint uq_auth_role_permission_bridge_permission_id unique (permission_id)
);

-- foreign keys and indices
create index ix_auth_identity_role_bridge_identity_id on auth_identity_role_bridge (identity_id);
alter table auth_identity_role_bridge add constraint fk_auth_identity_role_bridge_identity_id foreign key (identity_id) references auth_identity (id) on delete restrict on update restrict;

alter table auth_identity_role_bridge add constraint fk_auth_identity_role_bridge_role_id foreign key (role_id) references auth_role (id) on delete restrict on update restrict;

create index ix_auth_role_permission_bridge_role_id on auth_role_permission_bridge (role_id);
alter table auth_role_permission_bridge add constraint fk_auth_role_permission_bridge_role_id foreign key (role_id) references auth_role (id) on delete restrict on update restrict;

alter table auth_role_permission_bridge add constraint fk_auth_role_permission_bridge_permission_id foreign key (permission_id) references auth_permission (id) on delete restrict on update restrict;

