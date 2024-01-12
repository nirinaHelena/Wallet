-- create table transaction
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id serial PRIMARY KEY,
    account_id int REFERENCES account(account_id),
    transaction_label varchar,
    transaction_amount double precision NOT NULL,
    transaction_date_hour timestamp default CURRENT_TIMESTAMP,
    transaction_type varchar NOT NULL CHECK(transaction_type IN ('debit', 'credit'))
);