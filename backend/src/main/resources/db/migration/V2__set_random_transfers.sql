DO $$
DECLARE
    i INTEGER;
BEGIN
    FOR i IN 1..100 LOOP
        INSERT INTO transfers (client_id_from, client_id_to, account_number_from, account_number_to, transfer_sum, status, created_at, updated_at)
        VALUES (
            floor(random() * 1000)::bigint,
            floor(random() * 1000)::bigint,
            LPAD(floor(random() * 100000000000000)::bigint::text, 16, '0'),
            LPAD(floor(random() * 100000000000000)::bigint::text, 16, '0'),
            ROUND((random() * 1000)::numeric, 2),
            'SUCCESS',
            NOW(),
            NOW()
        );
    END LOOP;
END $$;