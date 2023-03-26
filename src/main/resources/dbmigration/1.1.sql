-- apply post alter
alter table dungeon_run add constraint uq_dungeon_run_character_sku_name unique  (character_sku,name);
alter table levelup_run add constraint uq_levelup_run_character_sku_name unique  (character_sku,name);
alter table raid_run add constraint uq_raid_run_character_sku_name unique  (character_sku,name);
