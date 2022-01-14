--CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1 INCREMENT BY 1;
--
--DROP TABLE IF EXISTS Employee;
--
--CREATE TABLE Employee
--(
--    id              INT AUTO_INCREMENT PRIMARY KEY,
--    name            VARCHAR(20) NOT NULL,
--    supervisor_id   INT,
--);

CREATE TABLE IF NOT EXISTS "employee" (
	"id"	        SERIAL,
	"name"	        varchar NOT NULL UNIQUE,
	"supervisor_id"	bigint
);