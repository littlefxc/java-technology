-- we don't know how to generate schema personal (class Schema) :(
create table if not exists article
(
  id bigint unsigned auto_increment
    primary key,
  content varchar(255) null,
  title varchar(255) null,
  url varchar(255) null
)
;

create table if not exists user
(
  id bigint unsigned auto_increment
    primary key,
  username varchar(255) charset utf8 null,
  password varchar(255) charset utf8 null
)
;
