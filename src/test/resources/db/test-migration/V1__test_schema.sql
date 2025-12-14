-- Test-only Flyway migration: create minimal schema (no seed data)

-- Create COUNTRY table
create table country (
    id          varchar(64)  not null,
    pid         varchar(64)  not null,
    name        varchar(255) not null,
    iso2        varchar(2)   not null,
    iso3        varchar(3)   not null,
    code        smallint     not null,
    created_at  timestamp    not null default current_timestamp,
    updated_at  timestamp    not null default TIMESTAMP '1000-01-01 00:00:00',

    constraint country_pk primary key (id),

    constraint country_pid_ux unique (pid),
    constraint country_name_ux unique (name),
    constraint country_iso2_ux unique (iso2),
    constraint country_iso3_ux unique (iso3),
    constraint country_code_ux unique (code)
);

-- Triggers are Postgres-specific; omit triggers for H2 test migration

-- Create CURRENCY table
create table currency (
    id          varchar(64)  not null,
    pid         varchar(64)  not null,
    name        varchar(255) not null,
    iso3        varchar(3)   not null,
    code        smallint     not null,
    symbol      varchar(10)  not null,
    created_at  timestamp    not null default current_timestamp,
    updated_at  timestamp    not null default TIMESTAMP '1000-01-01 00:00:00',

    constraint currency_pk primary key (id),

    constraint currency_pid_ux unique (pid),
    constraint currency_name_ux unique (name),
    constraint currency_iso3_ux unique (iso3),
    constraint currency_code_ux unique (code)
);

-- Triggers are Postgres-specific; omit triggers for H2 test migration

-- Create COUNTRY_CURRENCY table
create table country_currency (
    id          varchar(64)  not null,
    country_id  varchar(64)  not null,
    currency_id varchar(64)  not null,
    created_at  timestamp    not null default current_timestamp,

    constraint country_currency_pk primary key (id),
    constraint country_currency_pair_ux unique (country_id, currency_id),

    constraint country_currency_country_id_fk
        foreign key (country_id) references country(id) on delete cascade,

    constraint country_currency_currency_id_fk
        foreign key (currency_id) references currency(id) on delete cascade
);

create index country_currency_country_id_ix on country_currency (country_id);
create index country_currency_currency_id_ix on country_currency (currency_id);
