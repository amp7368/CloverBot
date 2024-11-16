-- drop dependencies
alter table service_status_notification drop constraint if exists ck_service_status_notification_success;
-- apply alter tables
alter table service_status_notification alter column success type varchar(8) using success::varchar(8);
-- apply post alter
alter table service_status_notification add constraint ck_service_status_notification_success check ( success in ('PENDING','SENT','FAILED','RETRYING','RESENT'));
