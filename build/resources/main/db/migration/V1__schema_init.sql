create extension if not exists citext;
create extension if not exists hstore;
create extension if not exists pg_trgm;
create extension if not exists pgcrypto;
create extension if not exists btree_gin;
create extension if not exists btree_gist;
create extension if not exists "uuid-ossp";
--
-- Function uuidv7()
--
create or replace function uuidv7()
    returns uuid language plpgsql
as $$
declare
    t_ms bigint;
    t_bytes bytea;
    r_bytes bytea;
begin
    t_ms := floor(extract(epoch from clock_timestamp()) * 1000)::bigint;

    t_bytes := e'\\000\\000\\000\\000\\000\\000';
    t_bytes := set_byte(t_bytes, 0, ((t_ms >> 40) & 255)::int);
    t_bytes := set_byte(t_bytes, 1, ((t_ms >> 32) & 255)::int);
    t_bytes := set_byte(t_bytes, 2, ((t_ms >> 24) & 255)::int);
    t_bytes := set_byte(t_bytes, 3, ((t_ms >> 16) & 255)::int);
    t_bytes := set_byte(t_bytes, 4, ((t_ms >> 8) & 255)::int);
    t_bytes := set_byte(t_bytes, 5, (t_ms & 255)::int);

    r_bytes := gen_random_bytes(10);

    return encode(t_bytes || r_bytes, 'hex')::uuid;
end;
$$;
--
-- Trigger function for updating updated_at field
--
create or replace function trigger_updated_at()
    returns trigger as $$
begin
   if row(new.*) is distinct from row(old.*) then
      new.updated_at = timezone('utc', now());
      return new;
   else
      return old;
   end if;
end;
$$ language 'plpgsql';
--
-- Delete from table if it exists
--
create or replace procedure delete_if_exists(in tbl varchar) as $$
declare
    is_exists boolean;
begin
   select exists (
      select from pg_tables
	    where schemaname = 'public'
	      and tablename = tbl
   ) into is_exists;
   if is_exists then
      raise notice '>>> deleting data from [%] table...', tbl;
	   execute 'delete from ' || tbl;
   end if;
end $$ language plpgsql;
