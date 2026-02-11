-- liquibase formatted sql

-- changeset oleksandr-chumak:002-create-managers-table
CREATE TABLE manager_statuses
(
    manager_status_id int8 PRIMARY KEY,
    status_name       varchar(50) NOT NULL
);
INSERT INTO manager_statuses
VALUES (1, 'active'),
       (2, 'terminated');

CREATE TABLE managers
(
    credentials_id    int8         NOT NULL,
    first_name        varchar(255) NOT NULL,
    last_name         varchar(255) NOT NULL,
    manager_status_id int8         NOT NULL,
    CONSTRAINT managers_pk PRIMARY KEY (credentials_id),
    CONSTRAINT managers_credentials_id_fk FOREIGN KEY (credentials_id) REFERENCES credentials (credentials_id) ON DELETE CASCADE,
    CONSTRAINT managers_status_fk FOREIGN KEY (manager_status_id) REFERENCES manager_statuses (manager_status_id)
);