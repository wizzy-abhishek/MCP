drop table if exists dog;
create table if not exists dog
(
    id          serial primary key,
    name        text not null,
    owner       text null,
    description text not null
);