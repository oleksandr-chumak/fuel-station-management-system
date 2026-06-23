-- liquibase formatted sql

-- changeset oleksandr-chumak:0017-add-status-to-fuel-station-fuel-tanks
ALTER TABLE fuel_station_fuel_tanks
    ADD COLUMN fuel_tank_status_id int8 NOT NULL DEFAULT 1;

CREATE INDEX ON fuel_station_fuel_tanks (fuel_station_id, fuel_tank_status_id);
