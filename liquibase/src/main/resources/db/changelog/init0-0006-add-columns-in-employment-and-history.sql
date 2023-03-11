--liquibase formatted sql

--changeset author:saurav id:007


ALTER TABLE employment_details ADD COLUMN other_income_amount numeric;
ALTER TABLE employment_details ADD COLUMN other_income_currency varchar;
ALTER TABLE employment_details ADD COLUMN employer_type varchar;
ALTER TABLE employment_details ADD COLUMN employment_type_for_salaried varchar;
ALTER TABLE employment_details ADD COLUMN is_profession_verified boolean;
ALTER TABLE employment_details ADD COLUMN profession_name varchar;

ALTER TABLE employment_details_history ADD COLUMN other_income_amount numeric;
ALTER TABLE employment_details_history ADD COLUMN other_income_currency varchar;
ALTER TABLE employment_details_history ADD COLUMN employer_type varchar;
ALTER TABLE employment_details_history ADD COLUMN employment_type_for_salaried varchar;
ALTER TABLE employment_details_history ADD COLUMN is_profession_verified boolean;
ALTER TABLE employment_details_history ADD COLUMN profession_name varchar;


--added exclusively for migration to be removed later

ALTER TABLE employment_details ADD COLUMN is_source_employment boolean;
ALTER TABLE employment_details ADD COLUMN updated_at_from_customer timestamp;
