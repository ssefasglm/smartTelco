CREATE TABLE plan_campaign (
                               id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               plan_id     BIGINT NOT NULL REFERENCES tariff_plan(id),
                               campaign_id BIGINT NOT NULL REFERENCES campaign(id),
                               active      BOOLEAN NOT NULL DEFAULT TRUE
);