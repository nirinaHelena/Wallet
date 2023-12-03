create table account(
    id serial primary key,
    username varchar(150) not null,
    email varchar(100) not null,
    balance float DEFAULT 0.0
);