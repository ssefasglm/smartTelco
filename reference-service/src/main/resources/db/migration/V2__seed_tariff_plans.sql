INSERT INTO tariff_plan (code, name, monthly_fee, data_quota_gb, voice_minutes, sms_count, commitment_months, valid_from, valid_to, active)
VALUES
    ('PLAN_BASIC_10',   'Basic 10 GB',   300.00, 10,  500, 100,  0, '2026-01-01', NULL, TRUE),
    ('PLAN_YOUTH_25',   'Youth 25 GB',   450.00, 25, 1000, 250,  0, '2026-01-01', NULL, TRUE),
    ('PLAN_PREMIUM_50', 'Premium 50 GB', 700.00, 50, 2000, 500, 12, '2026-01-01', NULL, TRUE),
    ('PLAN_FAMILY_100', 'Family 100 GB', 1000.00, 100, 5000, 1000, 12, '2026-01-01', NULL, TRUE);