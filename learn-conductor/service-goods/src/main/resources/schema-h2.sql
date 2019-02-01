create table `goods`
(
  id           bigint auto_increment primary key not null,
  gmt_create   date                              not null,
  gmt_modified date                              not null,
  name         varchar(50)                       not null,
  price        varchar(25)                       not null
);

create table `warehouse`
(
  id           bigint auto_increment primary key not null,
  gmt_create   date                              not null,
  gmt_modified date                              not null,
  goods_id     bigint unique                     not null,
  count        int                               not null
);

create table `user`
(
  id           bigint auto_increment primary key not null,
  gmt_create   date                              not null,
  gmt_modified date                              not null,
  name         varchar(50)                       not null,
  money        varchar(25)                       not null
);