--  Fonction pour calculer la somme des entrées et sorties d'argent entre une plage de dates donnée
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
-- Fonction pour calculer la somme des montants de chaque catégorie entre une plage de dates donnée, avec des valeurs nulles transformées en 0 :
CREATE OR REPLACE FUNCTION calculate_category_amounts(
    p_account_id INT,
    p_start_date_hour TIMESTAMP,
    p_end_date_hour TIMESTAMP
) RETURNS TABLE (
    restaurant DOUBLE PRECISION,
    salaire DOUBLE PRECISION
) AS
$$
BEGIN
    RETURN QUERY
    SELECT
        COALESCE(SUM(CASE WHEN tc.category_name = 'restaurant' THEN t.transaction_amount ELSE 0 END), 0) AS restaurant,
        COALESCE(SUM(CASE WHEN tc.category_name = 'salaire' THEN t.transaction_amount ELSE 0 END), 0) AS salaire
    FROM transaction t
    LEFT JOIN transaction_category tc ON t.category_id = tc.category_id
    WHERE t.account_id = p_account_id
        AND t.transaction_date_hour BETWEEN p_start_date_hour AND p_end_date_hour;
END;
$$ LANGUAGE plpgsql;
