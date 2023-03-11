--liquibase formatted sql

--changeset author:vaibhav-kaushik id:0010

create table provisional_trust_data
(
    id                          bigserial primary key,
    reference_id                varchar not null,
    customer_reference_id       uuid not null,
    entity_reference_id         uuid,
    key                         varchar not null,
    business_vertical           varchar,
    verification_status         varchar,
    verification_date           timestamp,
    version                     bigint,
    created_at                  timestamp,
    updated_at                  timestamp
);

create table provisional_trust_data_history
(
    id                          bigserial not null ,
    reference_id                varchar not null,
    customer_reference_id       uuid not null,
    entity_reference_id         uuid,
    key                         varchar not null,
    business_vertical           varchar,
    verification_status         varchar,
    verification_date           timestamp,
    rev                         bigint,
    REVTYPE                     int,
    primary key (id, rev)

);