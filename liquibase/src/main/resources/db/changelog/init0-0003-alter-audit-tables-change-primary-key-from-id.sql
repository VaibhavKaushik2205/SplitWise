--liquibase formatted sql

--changeset author:vaibhav kaushik id:004

alter table customer_history drop constraint customer_history_pkey;
alter table employment_details_history drop constraint employment_details_history_pkey;
alter table address_history drop constraint address_history_pkey;

alter table customer_history add primary key (id, rev);
alter table employment_details_history add primary key (id, rev);
alter table address_history add primary key (id, rev);

alter table customer_history alter column id set not null;
alter table employment_details_history alter column id set not null;
alter table address_history alter column id set not null;