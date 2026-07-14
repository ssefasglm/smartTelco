INSERT INTO campaign_rule (campaign_id, rule_type, operator, expected_value, mandatory)
VALUES
    (1, 'AGE_RANGE',         'BETWEEN', '18-26', TRUE),
    (1, 'CUSTOMER_SEGMENT',  'EQUALS',  'YOUTH', TRUE),
    (1, 'MIN_TENURE_MONTHS', 'GTE',     '6',     TRUE),
    (2, 'MIN_TENURE_MONTHS', 'GTE',     '12',    TRUE),
    (3, 'NO_ACTIVE_COMMITMENT', 'EQUALS', 'true', TRUE),
    (4, 'MIN_DATA_USAGE_GB', 'GTE',     '40',    TRUE);