-- create database wallet
CREATE database wallet;

-- connect to the database wallet
\c wallet

-- create table currency
CREATE TABLE IF NOT EXISTS currency(
    currency_id serial primary key,
    currency_name varchar(50) not null,
    currency_code varchar(50) not null unique
);
-- create table transaction
CREATE TABLE IF not EXISTS "transaction"(
    transaction_id serial primary key,
    account_id int references account(account_id),
    transaction_label varchar,
    transaction_amount double precision not null,
    transaction_date_hour timestamp,
    transaction_type varchar not null check( transaction_type = "debit" or "credit" )
);
-- create table amount
CREATE TABLE IF not EXISTS amount(
    account_id int references account(account_id),
    amount double precision not null,
    datetime timestamp
);
-- table account
CREATE TABLE IF NOT EXISTS account(
    account_id serial primary key,
    account_name varchar not null check(account_name ilike "courant" or "epargne"),
    account_currency int references currency(currency_id),
    account_type varchar not null check (account_type ilike "banque" or "espece" or "mobile money")
);