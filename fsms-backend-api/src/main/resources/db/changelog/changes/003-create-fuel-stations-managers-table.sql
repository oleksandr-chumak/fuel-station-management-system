-- liquibase formatted sql

-- changeset oleksandr-chumak:003-create-fuel-station-managers-table
CREATE TABLE fuel_station_managers
(
    fuel_station_id int8 NOT NULL,
    manager_id      int8 NOT NULL,
    CONSTRAINT fuel_station_managers_pk PRIMARY KEY (fuel_station_id, manager_id),
    CONSTRAINT fuel_station_managers_fuel_station_id_fk FOREIGN KEY (fuel_station_id) REFERENCES fuel_stations (fuel_station_id) ON DELETE CASCADE,
    CONSTRAINT fuel_station_managers_manager_id_fk FOREIGN KEY (manager_id) REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE INDEX ON fuel_station_managers (manager_id);
