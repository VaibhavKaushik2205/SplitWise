--liquibase formatted sql

--changeset author:saurav id:003

DROP INDEX customer_pan_number_idx;
DROP INDEX customer_email_idx;
DROP INDEX customer_phone_number_idx;
DROP INDEX address_reference_id_idx;
DROP INDEX employment_details_reference_id_idx;