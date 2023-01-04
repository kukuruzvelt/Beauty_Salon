insert into `test`.`Timeslot` value(default, '10:00:00');
insert into `test`.`Timeslot` value(default, '10:30:00');
insert into `test`.`Timeslot` value(default, '11:00:00');
insert into `test`.`Timeslot` value(default, '11:30:00');
insert into `test`.`Timeslot` value(default, '12:00:00');

insert into `test`.`role` value(default, "ADMIN");
insert into `test`.`role` value(default, "MASTER");
insert into `test`.`role` value(default, "USER");

insert into `test`.`user` value(default, "admin", "", 1, "admin_name", "admin_surname", 0, 0);
insert into `test`.`user` value(default, "user1", "", 3, "user_name1", "user_surname1", 1000, 0);
insert into `test`.`user` value(default, "master1", "", 2, "master_name1", "master_surname1", 0, 87);
insert into `test`.`user` value(default, "master2", "", 2, "master_name2", "master_surname2", 0, 72);

insert into `test`.`service` value(default, "service1", 100);
insert into `test`.`service` value(default, "service2", 200);
insert into `test`.`service` value(default, "service3", 300);
insert into `test`.`service` value(default, "service4", 400);

insert into `test`.`user_service` value(default, 3, 1);
insert into `test`.`user_service` value(default, 3, 2);
insert into `test`.`user_service` value(default, 4, 3);
insert into `test`.`user_service` value(default, 4, 4);

insert into `test`.`request` value(default, "2000-11-20", 1, 3, 3, 1, "TEST", 1);
insert into `test`.`request` value(default, "2000-11-20", 2, 3, 3, 2, "TEST", 1);