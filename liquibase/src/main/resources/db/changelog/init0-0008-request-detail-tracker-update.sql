--liquibase formatted sql

--changeset author:vaibhav-kaushik id:009

ALTER TABLE trust_data RENAME COLUMN source to business_vertical;
ALTER TABLE trust_data_history RENAME COLUMN source to business_vertical;
ALTER TABLE trust_data DROP COLUMN reply_topic;
ALTER TABLE trust_data_history DROP COLUMN reply_topic;
ALTER TABLE trust_data DROP COLUMN res_reference_id;
ALTER TABLE trust_data_history DROP COLUMN res_reference_id;

ALTER TABLE request_details_tracker RENAME COLUMN data to payload;
ALTER TABLE request_details_tracker DROP COLUMN category;
ALTER TABLE request_details_tracker DROP COLUMN business_vertical;

