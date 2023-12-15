CREATE OR REPLACE FUNCTION calculate_balance(
    p_account_id INT,
    p_start_date_hour TIMESTAMP,
    p_end_date_hour TIMESTAMP
) RETURNS DOUBLE PRECISION AS
$$
DECLARE
    total_balance DOUBLE PRECISION;
BEGIN
    SELECT COALESCE(SUM(CASE WHEN t.transaction_type = 'credit' THEN t.transaction_amount ELSE -t.transaction_amount END), 0)
    INTO total_balance
    FROM transaction t
    WHERE t.account_id = p_account_id
        AND t.transaction_date_hour BETWEEN p_start_date_hour AND p_end_date_hour;

    RETURN total_balance;
END;
$$ LANGUAGE plpgsql;
