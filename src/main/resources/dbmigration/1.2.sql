-- apply alter tables
alter table blacklist add column login_id uuid;
-- foreign keys and indices
alter table blacklist add constraint fk_blacklist_login_id foreign key (login_id) references login_queue (id) on delete restrict on update restrict;

