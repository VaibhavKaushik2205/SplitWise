--liquibase formatted sql

--changeset author:vaibhav-kaushik id:0011

ALTER TABLE provisional_trust_data ADD COLUMN value jsonb;
ALTER TABLE provisional_trust_data_history ADD COLUMN value jsonb;
