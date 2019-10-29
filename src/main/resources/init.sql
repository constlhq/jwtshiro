drop table if exists svail_user;

create table svail_user(
    username varchar(50),
    email varchar(50),
    cellphone varchar(15),
    password varchar(255),
    credentialsSalt varchar(255)
);