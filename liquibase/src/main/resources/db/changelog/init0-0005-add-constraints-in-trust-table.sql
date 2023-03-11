--liquibase formatted sql

--changeset author:vaibhav id:006

ALTER TABLE trust_data ADD CONSTRAINT trust_reference_id_constraint UNIQUE (reference_id);
CREATE INDEX IF NOT EXISTS customer_trust_data_idx ON trust_data (customer_reference_id);
CREATE INDEX IF NOT EXISTS customer_trust_data_and_key_idx ON trust_data (customer_reference_id, key);
