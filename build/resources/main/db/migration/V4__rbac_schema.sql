--
-- RBAC schema: roles, privileges, role_privileges, user_roles
--
create table rbac_role (
    id          varchar(64)  not null,
    pid         varchar(64)  not null,
    name        varchar(64)  not null,
    description varchar(255) not null default '',
    created_at  timestamp    not null default timezone('utc', now()),
    updated_at  timestamp    not null default '1000-01-01'::timestamp,

    constraint rbac_role_pk primary key (id),
    constraint rbac_role_pid_ux unique (pid),
    constraint rbac_role_name_ux unique (name)
);

create trigger rbac_role_updated_at_tg
    before update on rbac_role for each row execute procedure trigger_updated_at();

create table rbac_privilege (
    id          varchar(64)  not null,
    pid         varchar(64)  not null,
    name        varchar(64)  not null,
    description varchar(255) not null default '',
    created_at  timestamp    not null default timezone('utc', now()),
    updated_at  timestamp    not null default '1000-01-01'::timestamp,

    constraint rbac_privilege_pk primary key (id),
    constraint rbac_privilege_pid_ux unique (pid),
    constraint rbac_privilege_name_ux unique (name)
);

create trigger rbac_privilege_updated_at_tg
    before update on rbac_privilege for each row execute procedure trigger_updated_at();

create table rbac_role_privilege (
    id            varchar(64) not null,
    role_id       varchar(64) not null,
    privilege_id  varchar(64) not null,
    created_at    timestamp   not null default timezone('utc', now()),

    constraint rbac_role_privilege_pk primary key (id),
    constraint rbac_role_privilege_ux unique (role_id, privilege_id),
    constraint rbac_role_privilege_role_fk foreign key (role_id) references rbac_role(id) on delete cascade,
    constraint rbac_role_privilege_priv_fk foreign key (privilege_id) references rbac_privilege(id) on delete cascade
);

create index rbac_role_privilege_role_ix on rbac_role_privilege (role_id);
create index rbac_role_privilege_priv_ix on rbac_role_privilege (privilege_id);

--
-- user_roles (assuming user table exists with id)
--
create table rbac_user_role (
    id        varchar(64) not null,
    user_id   varchar(64) not null,
    role_id   varchar(64) not null,
    created_at timestamp  not null default timezone('utc', now()),

    constraint rbac_user_role_pk primary key (id),
    constraint rbac_user_role_ux unique (user_id, role_id),
    constraint rbac_user_role_role_fk foreign key (role_id) references rbac_role(id) on delete cascade
    -- constraint rbac_user_role_user_fk foreign key (user_id) references app_user(id) on delete cascade
);

create index rbac_user_role_user_ix on rbac_user_role (user_id);
create index rbac_user_role_role_ix on rbac_user_role (role_id);

--
-- Default roles
--
insert into rbac_role (id, pid, name, description) values
    (uuidv7(), uuidv7(), 'system', 'System administrator, all privileges'),
    (uuidv7(), uuidv7(), 'manager', 'Manager, elevated privileges'),
    (uuidv7(), uuidv7(), 'user', 'Regular user'),
    (uuidv7(), uuidv7(), 'guest', 'Guest, minimal privileges');

--
-- Default privileges (examples)
--
insert into rbac_privilege (id, pid, name, description) values
    (uuidv7(), uuidv7(), 'ALL', 'All privileges'),
    (uuidv7(), uuidv7(), 'READ', 'Read access'),
    (uuidv7(), uuidv7(), 'WRITE', 'Write access'),
    (uuidv7(), uuidv7(), 'DELETE', 'Delete access'),
    (uuidv7(), uuidv7(), 'MANAGE_USERS', 'Manage users'),
    (uuidv7(), uuidv7(), 'MANAGE_ROLES', 'Manage roles and privileges');

--
-- Assign all privileges to system, manager gets most, user/guest get minimal
--
insert into rbac_role_privilege (id, role_id, privilege_id) 
    select uuidv7(), r.id, p.id from rbac_role r, rbac_privilege p where r.name = 'system';
insert into rbac_role_privilege (id, role_id, privilege_id) 
    select uuidv7(), r.id, p.id from rbac_role r, rbac_privilege p where r.name = 'manager' and p.name in ('READ','WRITE','MANAGE_USERS','MANAGE_ROLES');
insert into rbac_role_privilege (id, role_id, privilege_id) 
    select uuidv7(), r.id, p.id from rbac_role r, rbac_privilege p where r.name = 'user' and p.name in ('READ','WRITE');
insert into rbac_role_privilege (id, role_id, privilege_id) 
    select uuidv7(), r.id, p.id from rbac_role r, rbac_privilege p where r.name = 'guest' and p.name = 'READ';