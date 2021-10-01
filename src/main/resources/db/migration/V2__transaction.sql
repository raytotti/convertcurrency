create table `transaction` (
    id uuid not null,
    user_id uuid not null,
    origin_currency varchar(3) not null,
    origin_value decimal not null,
    destination_currency varchar(3),
    conversion_rate decimal not null,
    created_at timestamp not null,
    primary key (id),
    foreign key (user_id) references `user`(id)
);