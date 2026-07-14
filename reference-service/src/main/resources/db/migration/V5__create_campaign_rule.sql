CREATE TABLE campaign_rule (
                               id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               campaign_id    BIGINT NOT NULL REFERENCES campaign(id),
                               rule_type      VARCHAR(50) NOT NULL,
                               operator       VARCHAR(20),
                               expected_value VARCHAR(100) NOT NULL,
                               mandatory      BOOLEAN NOT NULL DEFAULT TRUE
);