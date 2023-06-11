alter table TRAINER
    modify USERNAME varchar(32) not null,
    modify EMAIL varchar(100) not null,
    modify PHONE_NUMBER varchar(32) not null,
    modify LAST_NAME varchar(64) not null,
    modify NAME varchar(64) not null;
