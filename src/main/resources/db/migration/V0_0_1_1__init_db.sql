CREATE TABLE drones
(
    id               BIGINT NOT NULL,
    serial_number    VARCHAR(100),
    model            VARCHAR(255),
    drone_state            VARCHAR(255),
    weight_limit     INT,
    battery_capacity INT,
    CONSTRAINT drone_pk PRIMARY KEY (id)
);
CREATE TABLE medications
(
    id              BIGINT NOT NULL,
    medication_name VARCHAR(255),
    weight          INT,
    code            VARCHAR(255),
    image_url       VARCHAR(255),
    drone_id        BIGINT NOT NULL,
    CONSTRAINT medication_pk PRIMARY KEY (id),
    CONSTRAINT medication_drone_fk FOREIGN KEY (drone_id) REFERENCES drones (id)
);

create sequence drone_id_generator start 1;
create sequence medication_id_generator start 1;