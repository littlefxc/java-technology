create table book
(
  id         bigint unsigned auto_increment
    primary key,
  name       varchar(255)    null,
  article_id bigint unsigned null,
  user_id    bigint unsigned null
);

