create table `user`(
    id uuid not null,
    cpf varchar(14) not null,
    name varchar(256) not null,
    primary key (id)
);
alter table `user` add constraint user_uk UNIQUE(cpf);
