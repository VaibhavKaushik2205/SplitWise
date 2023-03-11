--liquibase formatted sql

--changeset author:vaibhav-kaushik id:008

create table external_ids
(
    id                          bigserial primary key,
    external_id                 varchar not null,
    customer_reference_id       uuid not null,
    type                        varchar,
    business_vertical           varchar,
    version                     bigint,
    created_at                  timestamp,
    updated_at                  timestamp,
    constraint external_id_customer_reference_id_type_unique_constraint
        unique (external_id, customer_reference_id, type)
);

create table external_ids_history
(
    id                          bigserial not null ,
    external_id                 varchar not null,
    customer_reference_id       uuid not null,
    type                        varchar,
    business_vertical           varchar,
    rev                         bigint,
    REVTYPE                     int,
    primary key (id, rev)

);

create index if not exists external_ids_updated_at_idx on external_ids (updated_at);
create index if not exists external_ids_external_id_idx on external_ids (external_id);
create index if not exists external_ids_customer_reference_id_idx on external_ids (customer_reference_id);
