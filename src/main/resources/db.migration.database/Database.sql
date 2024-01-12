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

--ALTER TABLE transaction
--ADD CONSTRAINT transaction_type
--CHECK (
  --  (transaction_type = 'debit' AND category_type = 'debit')
    --OR
    --(transaction_type = 'credit' AND category_type = 'credit')
--);
