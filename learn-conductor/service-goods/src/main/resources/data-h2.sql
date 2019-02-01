insert into goods(gmt_create, gmt_modified, `name`, price) values ( now(), now(), '咸鱼', '99.8');
insert into goods(gmt_create, gmt_modified, `name`, price) values ( now(), now(), 'Apple Mac Book Pro 2018', '27850');

insert into user(gmt_create, gmt_modified, `name`, money) values (now(), now(), 'fxc', '10000');

insert into warehouse(gmt_create, gmt_modified, goods_id, `count`) values (now(), now(), 1, 1000);
insert into warehouse(gmt_create, gmt_modified, goods_id, `count`) values (now(), now(), 2, 500);