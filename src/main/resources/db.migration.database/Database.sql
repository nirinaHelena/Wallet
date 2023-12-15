


-- TODO: update table transaction as "transaction"



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
