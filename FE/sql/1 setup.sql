CREATE SCHEMA IF NOT EXISTS file_migrator_app;


CREATE TABLE IF NOT EXISTS file_migrator_app.error_log_entry
(
	id					serial			PRIMARY KEY,
	table_name			varchar(100)	NOT NULL,
	record_id			integer			NOT NULL,
	record_sf_id		varchar(18)		NOT NULL,
	message				text,
	created_date		timestamptz 	NOT NULL DEFAULT NOW()
);

ALTER TABLE sf_archive.attachment ADD COLUMN adb_blob_migrated boolean DEFAULT FALSE;
ALTER TABLE sf_archive.contentversion ADD COLUMN adb_blob_migrated boolean DEFAULT FALSE;