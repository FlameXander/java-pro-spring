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

insert into clients (fio)
values ('B B B');

insert into accounts (client_id, account_number, balance)
values (2, '1234123412341234', 2000);

insert into clients (fio)
values ('C C C');

insert into accounts (client_id, account_number, balance)
values (3, '1234123412341234', 3000);

create table transfers
(
    id              bigserial primary key,
    request_id      bigserial,
    sender_id       bigint,
    send_account    varchar(16),
    receiver_id     bigint,
    receiv_account  varchar(16),
    amount          numeric(6, 2),
    document_status varchar(16),
    update_date_time timestamp
);
