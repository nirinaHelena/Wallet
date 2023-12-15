--create table transaction_category
CREATE TABLE IF NOT EXISTS transaction_category (
    category_id serial PRIMARY KEY,
    category_name varchar(50) UNIQUE NOT NULL,
    category_type varchar(50) NOT NULL CHECK(category_type IN ('debit', 'credit')),
);
