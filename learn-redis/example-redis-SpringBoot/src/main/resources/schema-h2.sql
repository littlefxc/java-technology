create table city(
  id int auto_increment primary key not null,
  province_id int not null ,
  city_name varchar(50) not null ,
  description varchar(255)
);

create table province(
  id int auto_increment primary key not null,
  province_name varchar(50) not null ,
  description varchar(255)
)