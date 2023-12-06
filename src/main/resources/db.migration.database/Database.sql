-- create database wallet
create database if not exist wallet;

-- create table currency
create table if not exist currency(
    currency_id serial primary key,
    currency_name varchar(50) not null,
    currency_code varchar(50) not null unique
);
-- create table transaction
create table if not exist 'transaction'(
    transaction_id serial primary key,
    transaction_label varchar,
    transaction_amount double not null,
    transaction_date_hour timestamp,
    transaction_type varchar not null check( transaction_type = "debit" or "credit" )
);
-- create table amount
create table if not exist amount(
    amount double not null,
    datetime timestamp
)
-- create table account check amount type column, transaction list
create table if not exist account(
    account_id serial primary key,
    account_name varchar not null check(account_name ilike "courant" or "epargne"),
    amount,
    transaction_list,
    account_currency int references currency(currency_id),
    account_type varchar not null check (account_type ilike "banque" or "espece" or "mobile money")
);