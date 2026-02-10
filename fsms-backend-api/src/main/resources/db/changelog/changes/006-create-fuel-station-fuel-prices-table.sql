-- liquibase formatted sql

-- changeset oleksandr-chumak:006-create-fuel-station-fuel-prices-table
CREATE TABLE fuel_station_fuel_prices
(
    fuel_station_id int8           NOT NULL,
    fuel_grade      varchar(50)    NOT NULL,
    price_per_liter numeric(10, 2) NOT NULL,
    CONSTRAINT fuel_station_fuel_prices_pk PRIMARY KEY (fuel_station_id, fuel_grade),
    CONSTRAINT fuel_station_fuel_prices_fuel_station_id_fk FOREIGN KEY (fuel_station_id) REFERENCES fuel_stations (fuel_station_id) ON DELETE CASCADE,
    CONSTRAINT fuel_station_fuel_prices_fuel_grade_check CHECK (fuel_grade IN ('RON_92', 'RON_95', 'DIESEL'))
);
