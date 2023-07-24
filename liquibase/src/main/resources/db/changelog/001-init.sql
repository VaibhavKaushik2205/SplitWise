--liquibase formatted sql

--changeset author: vaibhav id:001

create table users
(
    id                 bigserial primary key,
    reference_id       varchar not null unique,
    name               varchar,
    phone_number       varchar not null,
    email              varchar,
    version            bigint,
    created_at         timestamp,
    updated_at         timestamp,
    constraint user_reference_id_idx
        unique (reference_id)
);

create table user_history
(
    id                 bigserial primary key,
    reference_id       varchar not null,
    name               varchar,
    phone_number       varchar not null,
    email              varchar,
    rev                bigint,
    REVTYPE            int
);

create table split_group
(
    id                      bigserial primary key,
    group_reference_id      varchar unique ,
    name_of_group           varchar,
    user_splits             jsonb,
    version                 bigint,
    created_at              timestamp,
    updated_at              timestamp
);

create table user_group
(
    user_id             int8 not null,
    split_group_id      int8 not null,

    primary key (user_id, split_group_id)
);

create table expense
(
    id                  bigserial primary key,
    paid_by             bigint,
    group_id            bigint,
    amount              decimal,
    meta_data           jsonb,
    version             bigint,
    created_at          timestamp,
    updated_at          timestamp,

    foreign key (paid_by) references users
);

create table split
(
    id                    bigserial primary key,
    amount                decimal,
    user_name             varchar,
    user_reference_id     varchar,
    expense_id            bigint not null,
    version               bigint,
    created_at            timestamp,
    updated_at            timestamp,

    foreign key (expense_id) references expense
);

create table split_group_expense
(
    split_group_id      int8 not null,
    expense_id          int8 not null,

    foreign key (expense_id) references expense,
    foreign key (split_group_id) references split_group
);

-- create table split_group_expense
-- (
--     split_group_id      int8 not null,
--     expense_id          int8 not null
-- );

-- create table split_group_user_splits
-- (
--     split_group_id      int8 not null,
--     user_splits_id      int8 not null
-- );

create table request_details
(
    id                     bigserial PRIMARY KEY,
    request_id             uuid,
    request_type           varchar,
    reply_topic            varchar,
    status                 varchar,
    payload                jsonb,
    version                bigint,
    created_at             timestamp,
    updated_at             timestamp,

    constraint request_details_unique_constraint
        unique (request_id, request_type)
);


-- // for auditing
create table revinfo
(
    rev         BIGSERIAL PRIMARY KEY,
    revtstmp    bigint
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;
create index if not exists user_reference_id_idx on users (reference_id);
create unique index if not exists user_phone_number_idx on users (phone_number);
create unique index if not exists users_email_idx on users (email);
create index if not exists users_created_at_idx on users (created_at);
create index if not exists users_updated_at_idx on users (updated_at);
create unique index if not exists group_reference_id_idx on split_group (group_reference_id);

create index if not exists request_details_unique_constraint on request_details (request_id, request_type);