CREATE TABLE tariff_plan (
                             id                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                             code              VARCHAR(50) NOT NULL UNIQUE,
                             name              VARCHAR(100) NOT NULL,
                             monthly_fee       NUMERIC(10, 2) NOT NULL,
                             data_quota_gb     INTEGER NOT NULL,
                             voice_minutes     INTEGER NOT NULL,
                             sms_count         INTEGER NOT NULL,
                             commitment_months INTEGER NOT NULL DEFAULT 0,
                             valid_from        DATE NOT NULL,
                             valid_to          DATE,
                             active            BOOLEAN NOT NULL DEFAULT TRUE
);