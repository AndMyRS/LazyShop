create schema if not exists postgres;

create table postgres.t_product
(
    id serial primary key,
    c_name varchar(45) not null check (length(trim(c_name)) >= 3),
    c_description varchar(250)
);