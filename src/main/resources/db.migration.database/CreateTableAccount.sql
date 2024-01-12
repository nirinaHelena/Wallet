-- create table account
CREATE TABLE IF NOT EXISTS account (
    account_id serial PRIMARY KEY,
    account_name varchar NOT NULL CHECK(account_name ILIKE 'courant' OR account_name ILIKE 'epargne'),
    account_currency int REFERENCES currency(currency_id),
    account_type varchar NOT NULL CHECK(account_type ILIKE 'banque' OR account_type ILIKE 'espece' OR account_type ILIKE 'mobile money')
);