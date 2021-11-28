CREATE TABLE IF NOT EXISTS "employee" (
	"id"	integer NOT NULL,
	"name"	varchar NOT NULL UNIQUE ON CONFLICT IGNORE,
	"supervisor_id"	integer,
	PRIMARY KEY("id")
);