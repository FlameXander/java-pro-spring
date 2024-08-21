create table accounts
(
    id             bigserial primary key,
    client_id      bigint,
    account_number varchar(16),
    balance        numeric(6, 2),
    created_at     timestamp,
    updated_at     timestamp
);

insert into accounts (client_id, account_number, balance)
values (1, '1234123412341234', 1000);

CREATE TABLE limits (
id BIGSERIAL PRIMARY KEY,
user_id BIGINT NOT NULL,
amount BIGINT NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
