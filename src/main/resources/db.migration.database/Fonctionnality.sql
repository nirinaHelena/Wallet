-- show all account whith all args
SELECT
    a.account_id AS "ID",
    a.account_name AS "Nom",
    COALESCE(SUM(am.amount), 0) AS "Solde",
    MAX(am.datetime) AS "DateDerniereMiseAJour",
    t.transaction_label AS "TransactionLabel",
    t.transaction_amount AS "MontantTransaction",
    t.transaction_date_hour AS "DateHeureTransaction",
    t.transaction_type AS "TypeTransaction",
    c.currency_name AS "Devise",
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
