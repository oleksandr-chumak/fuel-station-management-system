-- liquibase formatted sql

-- changeset oleksandr-chumak:003-create-administrators-table
CREATE TABLE administrators
(
    credentials_id int8 NOT NULL,
    CONSTRAINT administrators_pk PRIMARY KEY (credentials_id),
    CONSTRAINT administrators_credentials_id_fk FOREIGN KEY (credentials_id) REFERENCES credentials (credentials_id) ON DELETE CASCADE
);