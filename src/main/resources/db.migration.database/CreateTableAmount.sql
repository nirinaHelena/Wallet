-- create table amount
CREATE TABLE IF NOT EXISTS amount (
    account_id int REFERENCES account(account_id),
    amount double precision NOT NULL,
    datetime timestamp
);
