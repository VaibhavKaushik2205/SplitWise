--liquibase formatted sql

--changeset author:vaibhav-kaushik id:013

ALTER TABLE provisional_trust_data alter column reference_id drop not null;
ALTER TABLE provisional_trust_data_history alter column reference_id drop not null;