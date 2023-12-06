-- create database wallet
create database if not exist wallet;

-- TODO: create table currency
create table if not exist currency(
    currency_id serial primary key,
    currency_name varchar(50) not null,
    currency_code varchar(50) not null unique
);
-- TODO: create table transaction
-- TODO: create table amount
-- TODO: create table account