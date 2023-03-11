--liquibase formatted sql

--changeset author:sharduls id:001

create table customer
(
    id                 bigserial primary key,
    reference_id       uuid not null,
    name               varchar,
    phone_number       varchar not null,
    email              varchar,
    dob                varchar,
    pan                varchar,
    citizenship        varchar,
    father_name        varchar,
    mother_name        varchar,
    gender             varchar,
    marital_status     varchar,
    entity_type        varchar,
    source             varchar,
    version            bigint,
    created_at         timestamp,
    updated_at         timestamp,
    constraint customer_ref_id_unique_constraint
        unique (reference_id)
);

create table customer_history
(
    id                 bigserial primary key,
    reference_id       uuid not null,
    name               varchar,
    phone_number       varchar not null,
    email              varchar,
    dob                varchar,
    pan                varchar,
    citizenship        varchar,
    father_name        varchar,
    mother_name        varchar,
    gender             varchar,
    marital_status     varchar,
    entity_type        varchar,
    source             varchar,
    rev                bigint,
    REVTYPE            int
);

create table employment_details
(
    id                    bigserial primary key,
    reference_id          uuid not null,
    customer_reference_id uuid not null,
    employment_type       varchar,
    employer_name         varchar,
    income_currency       varchar,
    income_amount         numeric,
    work_email            varchar,
    work_industry         varchar,
    profession            varchar,
    registration_number   varchar,
    registration_date     date,
    registration_entity   varchar,
    source                varchar not null,
    version               bigint,
    created_at            timestamp,
    updated_at            timestamp,
    constraint customer_reference_id_unique_constraint
        unique (reference_id, source)
);

create table employment_details_history
(
    id                    bigserial primary key,
    reference_id          uuid not null,
    customer_reference_id uuid not null,
    employment_type       varchar,
    employer_name         varchar,
    income_currency       varchar,
    income_amount         numeric,
    work_email            varchar,
    work_industry         varchar,
    profession            varchar,
    registration_number   varchar,
    registration_date     date,
    registration_entity   varchar,
    source                varchar not null,
    rev                   bigint,
    REVTYPE               int

);

create table address
(
    id                     bigserial primary key,
    reference_id           uuid not null,
    customer_reference_id  uuid not null,
    source                 varchar not null,
    house_number           varchar,
    street                 varchar,
    line_one               varchar,
    line_two               varchar,
    locality               varchar,
    city                   varchar,
    state                  varchar,
    type                   varchar,
    pin_code               varchar not null ,
    document_temp_url      varchar,
    document_type          varchar,
    is_current             boolean,
    version                bigint,
    created_at             timestamp,
    updated_at             timestamp,
    constraint address_customer_reference_id_unique_constraint
        unique (customer_reference_id, type)
);

create table address_history
(
    id                     bigserial primary key,
    reference_id           uuid not null ,
    customer_reference_id  uuid not null,
    source                 varchar not null,
    house_number           varchar,
    street                 varchar,
    line_one               varchar,
    line_two               varchar,
    locality               varchar,
    city                   varchar,
    state                  varchar,
    type                   varchar,
    pin_code               varchar not null ,
    document_temp_url      varchar,
    document_type          varchar,
    is_current             boolean,
    rev                    bigint,
    REVTYPE                int
);

CREATE TABLE request_details_tracker (
    id                     bigserial PRIMARY KEY,
    request_id             uuid,
    source                 varchar,
    request_type           varchar,
    reply_topic            varchar,
    status                 varchar,
    category               varchar,
    data                   jsonb,
    version                bigint,
    created_at             timestamp,
    updated_at             timestamp,
    constraint request_details_unique_constraint
        unique (request_id, request_type, category)
);


create table revinfo
(
    rev         BIGSERIAL PRIMARY KEY ,
    revtstmp    bigint
);

CREATE SEQUENCE IF NOT EXISTS hibernate_sequence START 1;
create index if not exists customer_pan_number_idx on customer (pan);
create index if not exists customer_phone_number_idx on customer (phone_number);
create index if not exists customer_email_idx on customer (email);
create index if not exists customer_created_at_idx on customer  (created_at);
create index if not exists customer_updated_at_index on customer (updated_at);
create index if not exists address_customer_reference_id_idx on address (customer_reference_id);
create index if not exists employment_details_customer_reference_id_idx on employment_details (customer_reference_id);

