-- create table transfer_history
CREATE TABLE IF NOT EXISTS transfer_history (
    transfer_id serial PRIMARY KEY,
    sender_account_id int REFERENCES account(account_id),
    receiver_account_id int REFERENCES account(account_id),
    transfer_amount double precision NOT NULL,
    transfer_date_hour timestamp DEFAULT CURRENT_TIMESTAMP
);
