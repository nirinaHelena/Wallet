-- create table currency
CREATE TABLE IF NOT EXISTS currency (
    currency_id serial PRIMARY KEY,
    currency_name varchar(50) NOT NULL,
    currency_code varchar(50) NOT NULL UNIQUE
);

-- create table account
CREATE TABLE IF NOT EXISTS account (
    account_id serial PRIMARY KEY,
    account_name varchar NOT NULL CHECK(account_name ILIKE 'courant' OR account_name ILIKE 'epargne'),
    account_currency int REFERENCES currency(currency_id),
    account_type varchar NOT NULL CHECK(account_type ILIKE 'banque' OR account_type ILIKE 'espece' OR account_type ILIKE 'mobile money')
);
-- TODO: update table transaction as "transaction"
-- create table transaction
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id serial PRIMARY KEY,
    account_id int REFERENCES account(account_id),
    transaction_label varchar,
    transaction_amount double precision NOT NULL,
    transaction_date_hour timestamp,
    transaction_type varchar NOT NULL CHECK(transaction_type IN ('debit', 'credit'))
);

-- create table amount
CREATE TABLE IF NOT EXISTS amount (
    account_id int REFERENCES account(account_id),
    amount double precision NOT NULL,
    datetime timestamp
);

-- create table transfer_history
CREATE TABLE IF NOT EXISTS transfer_history (
    transfer_id serial PRIMARY KEY,
    sender_account_id int REFERENCES account(account_id),
    receiver_account_id int REFERENCES account(account_id),
    transfer_amount double precision NOT NULL,
    transfer_date_hour timestamp DEFAULT CURRENT_TIMESTAMP
);

--create table transaction_category
CREATE TABLE IF NOT EXISTS transaction_category (
    category_id serial PRIMARY KEY,
    category_name varchar(50) UNIQUE NOT NULL,
    category_type varchar(50) NOT NULL CHECK(category_type IN ('debit', 'credit')),
);

-- update table transaction to add catégorie
ALTER TABLE transaction
ADD COLUMN category_id int REFERENCES transaction_category(category_id);

-- add unique constraint on category_name in transaction_category
ALTER TABLE transaction_category
ADD CONSTRAINT unique_category_name UNIQUE (category_name);

-- add NOT NULL constraint on category_id in transaction
ALTER TABLE transaction
ALTER COLUMN category_id SET NOT NULL;

-- update CHECK constraint on transaction_type in transaction
ALTER TABLE transaction
DROP CONSTRAINT IF EXISTS valid_transaction_type;

ALTER TABLE transaction
ADD CONSTRAINT valid_transaction_type
CHECK (
    (transaction_type = 'debit' AND category_id IS NOT NULL AND EXISTS (SELECT 1 FROM transaction_category WHERE category_id = transaction.category_id AND category_type = 'debit'))
    OR
    (transaction_type = 'credit' AND category_id IS NOT NULL AND EXISTS (SELECT 1 FROM transaction_category WHERE category_id = transaction.category_id AND category_type = 'credit'))
);
