--liquibase formatted sql

--changeset author:saurav id:002

ALTER TABLE employment_details DROP CONSTRAINT customer_reference_id_unique_constraint;
ALTER TABLE address DROP CONSTRAINT address_customer_reference_id_unique_constraint;
CREATE INDEX IF NOT EXISTS address_reference_id_idx ON address (reference_id);
CREATE INDEX IF NOT EXISTS employment_details_reference_id_idx ON employment_details (reference_id);

ALTER TABLE address ALTER pin_code DROP NOT NULL;
ALTER TABLE address_history ALTER pin_code DROP NOT NULL;

ALTER TABLE address RENAME COLUMN document_temp_url TO document_url;
ALTER TABLE address RENAME COLUMN document_type TO address_source;
ALTER TABLE address_history RENAME COLUMN document_temp_url TO document_url;
ALTER TABLE address_history RENAME COLUMN document_type TO address_source;

DROP INDEX customer_created_at_idx;
DROP INDEX customer_updated_at_index;

ALTER TABLE customer ALTER COLUMN dob TYPE date using (dob::TEXT::date);
ALTER TABLE customer_history ALTER COLUMN dob TYPE date using (dob::TEXT::date);
ALTER TABLE customer ADD CONSTRAINT customer_phone_number_unique_idx UNIQUE (phone_number);

ALTER TABLE employment_details ALTER COLUMN income_amount TYPE numeric(36,6);
ALTER TABLE employment_details_history ALTER COLUMN income_amount TYPE numeric(36,6);

ALTER TABLE employment_details ADD CONSTRAINT employment_details_reference_id_unique_idx UNIQUE (reference_id);
ALTER TABLE address ADD CONSTRAINT address_reference_id_unique_idx UNIQUE (reference_id);

ALTER TABLE employment_details DROP COLUMN registration_date, DROP COLUMN registration_number, DROP COLUMN registration_entity;
ALTER TABLE employment_details_history DROP COLUMN registration_date, DROP COLUMN registration_number, DROP COLUMN registration_entity;
ALTER TABLE employment_details ADD COLUMN professional_data jsonb;
ALTER TABLE employment_details_history ADD COLUMN professional_data jsonb;

ALTER TABLE address RENAME COLUMN source TO business_vertical;
ALTER TABLE address_history RENAME COLUMN source TO business_vertical;
ALTER TABLE customer RENAME COLUMN source TO business_vertical;
ALTER TABLE customer_history RENAME COLUMN source TO business_vertical;
ALTER TABLE employment_details RENAME COLUMN source TO business_vertical;
ALTER TABLE employment_details_history RENAME COLUMN source TO business_vertical;
ALTER TABLE request_details_tracker RENAME COLUMN source TO business_vertical;

