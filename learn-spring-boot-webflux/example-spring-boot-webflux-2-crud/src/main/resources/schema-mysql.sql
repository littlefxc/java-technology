use personal;
create table if not exists city
(
  id      bigint auto_increment primary key,
  province_id bigint null,
  city_name   varchar(255) null,
  description     varchar(255) null
) CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci
  ENGINE = InnoDB;