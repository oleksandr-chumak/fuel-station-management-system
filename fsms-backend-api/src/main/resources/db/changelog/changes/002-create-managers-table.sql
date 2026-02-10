-- liquibase formatted sql

-- changeset oleksandr-chumak:002-create-managers-table
CREATE TABLE managers
(
    credentials_id int8         NOT NULL,
    first_name     varchar(255) NOT NULL,
    last_name      varchar(255) NOT NULL,
    status         varchar(50)  NOT NULL,
    CONSTRAINT managers_pk PRIMARY KEY (credentials_id),
    CONSTRAINT managers_credentials_id_fk FOREIGN KEY (credentials_id) REFERENCES credentials (credentials_id) ON DELETE CASCADE,
    CONSTRAINT managers_status_check CHECK (status IN ('ACTIVE', 'TERMINATED'))
);