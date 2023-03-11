--liquibase formatted sql

--changeset author:vaibhav-kaushik id:012

CREATE INDEX IF NOT EXISTS customer_pan_idx ON customer (pan);

CREATE INDEX IF NOT EXISTS customer_provisional_trust_data_idx ON provisional_trust_data (customer_reference_id);
CREATE INDEX IF NOT EXISTS customer_provisional_trust_data_and_key_idx ON provisional_trust_data (customer_reference_id, key);
