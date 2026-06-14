-- liquibase formatted sql

-- changeset oleksandr-chumak:0015-create-countries-table
CREATE TABLE countries
(
    country_id   int8         PRIMARY KEY,
    country_code varchar(2)   NOT NULL UNIQUE
);
INSERT INTO countries (country_id, country_code)
VALUES (1, 'DE'),
       (2, 'NO'),
       (3, 'UA');

-- Migrate fuel_stations.country (varchar) -> fuel_stations.country_id (int8 FK)
ALTER TABLE fuel_stations
    ADD COLUMN country_id int8;

UPDATE fuel_stations fs
SET country_id = c.country_id
FROM countries c
WHERE fs.country = c.country_code;

ALTER TABLE fuel_stations
    ALTER COLUMN country_id SET NOT NULL;

ALTER TABLE fuel_stations
    DROP CONSTRAINT chk_country_code;

ALTER TABLE fuel_stations
    DROP COLUMN country;

ALTER TABLE fuel_stations
    ADD CONSTRAINT fuel_stations_country_fk
        FOREIGN KEY (country_id) REFERENCES countries (country_id);

-- Migrate fuel_tax_rules.country_code (varchar) -> fuel_tax_rules.country_id (int8 FK)
ALTER TABLE fuel_tax_rules
    ADD COLUMN country_id int8;

UPDATE fuel_tax_rules ftr
SET country_id = c.country_id
FROM countries c
WHERE ftr.country_code = c.country_code;

ALTER TABLE fuel_tax_rules
    ALTER COLUMN country_id SET NOT NULL;

DROP INDEX idx_fuel_tax_rules_country_grade_type_eff;

ALTER TABLE fuel_tax_rules
    DROP COLUMN country_code;

ALTER TABLE fuel_tax_rules
    ADD CONSTRAINT fuel_tax_rules_country_fk
        FOREIGN KEY (country_id) REFERENCES countries (country_id);

CREATE INDEX idx_fuel_tax_rules_country_id_grade_type_eff
    ON fuel_tax_rules (country_id, fuel_grade_id, tax_type, effective_from);
