INSERT INTO campaign (code, name, discount_type, discount_value, priority, combinable, valid_from, valid_to, active)
VALUES
    ('YOUTH_20',         'Gençlere %20 İndirim',      'PERCENTAGE',   20.00, 10, FALSE, '2026-01-01', NULL, TRUE),
    ('LOYALTY_50',       'Sadakat 50 TL İndirim',     'FIXED_AMOUNT', 50.00,  5, FALSE, '2026-01-01', NULL, TRUE),
    ('NO_COMMITMENT_10', 'Taahhütsüz %10 İndirim',    'PERCENTAGE',   10.00,  3, TRUE,  '2026-01-01', NULL, TRUE),
    ('PREMIUM_DATA_15',  'Premium Veri %15 İndirim',  'PERCENTAGE',   15.00,  7, FALSE, '2026-01-01', NULL, TRUE);