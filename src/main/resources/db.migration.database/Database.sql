-- create database wallet
CREATE DATABASE wallet;

-- connect to the database wallet
\c wallet;

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
    transfer_date_hour timestamp,
    transfer_description varchar
);
