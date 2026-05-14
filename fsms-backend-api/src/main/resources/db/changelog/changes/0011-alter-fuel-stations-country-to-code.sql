--liquibase formatted sql
--changeset system:0011-alter-fuel-stations-country-to-code
--preconditions onFail:MARK_RAN
--precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.table_constraints WHERE constraint_name='chk_country_code' AND table_name='fuel_stations'

UPDATE fuel_stations SET country = 'UA' WHERE country = 'Ukraine';
UPDATE fuel_stations SET country = 'NO' WHERE country = 'Norway';
UPDATE fuel_stations SET country = 'DE' WHERE country IN ('Germany', 'Poland');
UPDATE fuel_stations SET country = 'UA' WHERE country = 'Тест';

ALTER TABLE fuel_stations
    ADD CONSTRAINT chk_country_code CHECK (country IN ('DE', 'NO', 'UA'));
