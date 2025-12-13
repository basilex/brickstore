--
-- Remove pid from country_currency association table
-- pid is not needed on junction tables
--

-- Drop the unique constraint on pid
ALTER TABLE country_currency DROP CONSTRAINT country_currency_pid_ux;

-- Drop the pid column
ALTER TABLE country_currency DROP COLUMN pid;
