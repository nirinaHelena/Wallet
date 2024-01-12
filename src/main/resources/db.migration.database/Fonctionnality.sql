-- show all account with all args
SELECT
    a.account_id AS "ID",
    a.account_name AS "Nom",
    COALESCE(SUM(am.amount), 0) AS "Solde",
    MAX(am.datetime) AS "DateDerniereMiseAJour",
    t.transaction_label AS "TransactionLabel",
    t.transaction_amount AS "MontantTransaction",
    t.transaction_date_hour AS "DateHeureTransaction",
    t.transaction_type AS "TypeTransaction",
    c.currency_name AS "Devise",CREATE OR REPLACE FUNCTION calculate_category_amounts(
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

    a.account_type AS "Type"
FROM
    account a
LEFT JOIN
    amount am ON a.account_id = am.account_id
LEFT JOIN
    transaction t ON a.account_id = t.account_id
LEFT JOIN
    currency c ON a.account_currency = c.currency_id
GROUP BY
    a.account_id, a.account_name, c.currency_name, a.account_type,
    t.transaction_label, t.transaction_amount, t.transaction_date_hour, t.transaction_type
ORDER BY
    a.account_id, t.transaction_date_hour;


-- show amount between
SELECT *
FROM amount
WHERE
    account_id = <account_id> AND
    datetime BETWEEN '2023-01-01' AND '2023-12-31';


-- show amount today
SELECT amount
FROM amount
WHERE account_id = account_id_param
ORDER BY datetime DESC
LIMIT 1;


-- show amount at this date
-- Sélectionner le montant correspondant au compte et à la date donnée
SELECT COALESCE(SUM(amount), 0) AS current_balance
FROM amount
WHERE
    account_id = account_id_param AND
    datetime <= datetime_param
ORDER BY datetime DESC
LIMIT 1;


