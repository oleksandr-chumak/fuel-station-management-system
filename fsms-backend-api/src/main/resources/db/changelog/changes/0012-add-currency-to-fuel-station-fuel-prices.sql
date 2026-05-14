-- liquibase formatted sql

-- changeset oleksandr-chumak:0012-add-currency-to-fuel-station-fuel-prices
ALTER TABLE fuel_station_fuel_prices
    ADD COLUMN currency varchar(8) NOT NULL DEFAULT 'EUR';
