-- create table currency
CREATE TABLE IF NOT EXISTS currency (
    currency_id serial PRIMARY KEY,
    currency_name varchar(50) NOT NULL,
    currency_code varchar(50) NOT NULL UNIQUE
);