create table limits
(
    id            bigserial primary key,
    client_id     bigint,
    limit_amount  numeric(8, 2),
    limit_max     numeric(8, 2),
    created_at    timestamp,
    updated_at    timestamp
);

insert into limits (client_id, limit_amount, limit_max)
values (1, 20000.00, 20000.00);