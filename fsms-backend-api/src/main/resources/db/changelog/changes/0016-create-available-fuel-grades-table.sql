-- liquibase formatted sql

-- changeset oleksandr-chumak:0016-create-available-fuel-grades-table
CREATE TABLE available_fuel_grades
(
    country_id    int8 NOT NULL,
    fuel_grade_id int8 NOT NULL,
    CONSTRAINT available_fuel_grades_pk PRIMARY KEY (country_id, fuel_grade_id),
    CONSTRAINT available_fuel_grades_country_fk
        FOREIGN KEY (country_id) REFERENCES countries (country_id),
    CONSTRAINT available_fuel_grades_fuel_grade_fk
        FOREIGN KEY (fuel_grade_id) REFERENCES fuel_grades (fuel_grade_id)
);

INSERT INTO available_fuel_grades (country_id, fuel_grade_id)
VALUES
    -- Germany (1): RON_95, DIESEL
    (1, 2), (1, 3),
    -- Norway (2): RON_95, DIESEL
    (2, 2), (2, 3),
    -- Ukraine (3): RON_92, RON_95, DIESEL
    (3, 1), (3, 2), (3, 3);
