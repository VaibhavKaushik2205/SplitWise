--liquibase formatted sql

--changeset author:vaibhav-kaushik id:005

create table trust_data
(
    id                          bigserial primary key,
    reference_id                varchar not null,
    customer_reference_id       uuid not null,
    entity_reference_id         uuid,
    key                         varchar not null,
    source                      varchar,
    res_reference_id            varchar,
    verification_status         varchar,
    verification_date           timestamp,
    reply_topic                 varchar,
    version                     bigint,
    created_at                  timestamp,
    updated_at                  timestamp
);

create table trust_data_history
(
    id                          bigserial not null ,
    reference_id                varchar not null,
    customer_reference_id       uuid not null,
    entity_reference_id         uuid,
    key                         varchar not null,
    source                      varchar,
    res_reference_id            varchar,
    verification_status         varchar,
    verification_date           timestamp,
    reply_topic                 varchar,
    rev                         bigint,
    REVTYPE                     int,
    primary key (id, rev)

);