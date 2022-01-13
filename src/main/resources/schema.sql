CREATE TABLE IF NOT EXISTS "employee" (
	"id"	        SERIAL,
	"name"	        varchar NOT NULL UNIQUE,
	"supervisor_id"	bigint
);