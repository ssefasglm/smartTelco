CREATE TABLE campaign (
                          id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          code           VARCHAR(50) NOT NULL UNIQUE,
                          name           VARCHAR(100) NOT NULL,
                          discount_type  VARCHAR(20) NOT NULL,
                          discount_value NUMERIC(10, 2) NOT NULL,
                          priority       INTEGER NOT NULL DEFAULT 0,
                          combinable     BOOLEAN NOT NULL DEFAULT FALSE,
                          valid_from     DATE NOT NULL,
                          valid_to       DATE,
                          active         BOOLEAN NOT NULL DEFAULT TRUE
);