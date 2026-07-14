CREATE TABLE tax_definition (
                                id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                code       VARCHAR(50) NOT NULL UNIQUE,
                                name       VARCHAR(100) NOT NULL,
                                rate       NUMERIC(5, 2) NOT NULL,
                                valid_from DATE NOT NULL,
                                valid_to   DATE,
                                active     BOOLEAN NOT NULL DEFAULT TRUE
);