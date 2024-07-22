create table accounts
(
    id             bigserial primary key,
    client_id      bigint,
    account_number varchar(16),
    balance        numeric(6, 2),
    created_at     timestamp,
    updated_at     timestamp
);

create table clients
(
    id  bigserial primary key,
    fio varchar(255)
);

insert into clients (fio)
values ('A A A');

insert into accounts (client_id, account_number, balance)
values (1, '1234123412341234', 1000);

create table transfers
(
    id                bigserial primary key,
    source_account_id bigint not null references accounts(id),
    target_account_id bigint not null references accounts(id),
    amount            numeric(10, 2) not null,
    status            varchar(20) not null,
    created_at        timestamp default current_timestamp,
    updated_at        timestamp default current_timestamp
);