-- liquibase formatted sql

-- changeset oleksandr-chumak:0020-create-fuel-order-allocations.sql

ALTER TABLE fuel_orders
    DROP COLUMN amount;

ALTER TABLE fuel_orders
    ADD COLUMN created_by int8;

ALTER TABLE fuel_orders
    ADD
        CONSTRAINT fuel_orders_created_by
            FOREIGN KEY (created_by) REFERENCES users (user_id) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE TABLE fuel_order_allocations
(
    fuel_order_id int8,
    fuel_tank_id  int8,
    volume        numeric(12, 3) NOT NULL,
    PRIMARY KEY (fuel_order_id, fuel_tank_id),
    CONSTRAINT fuel_order_allocations_fuel_order_id_fk
        FOREIGN KEY (fuel_order_id) REFERENCES fuel_orders (fuel_order_id)
            ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fuel_order_allocations_fuel_tank_id_fk
        FOREIGN KEY (fuel_tank_id) REFERENCES fuel_station_fuel_tanks (fuel_tank_id)
            ON DELETE RESTRICT ON UPDATE RESTRICT
);
