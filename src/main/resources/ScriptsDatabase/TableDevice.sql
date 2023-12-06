create table  if not exist device(
    id serial primary key,
    device varchar not null,
    country varchar not null
);