insert into `mydb`.`Timeslot` value(default, '10:00:00');
insert into `mydb`.`Timeslot` value(default, '10:30:00');
insert into `mydb`.`Timeslot` value(default, '11:00:00');
insert into `mydb`.`Timeslot` value(default, '11:30:00');
insert into `mydb`.`Timeslot` value(default, '12:00:00');
insert into `mydb`.`Timeslot` value(default, '12:30:00');
insert into `mydb`.`Timeslot` value(default, '13:00:00');
insert into `mydb`.`Timeslot` value(default, '14:30:00');
insert into `mydb`.`Timeslot` value(default, '14:00:00');
insert into `mydb`.`Timeslot` value(default, '14:30:00');
insert into `mydb`.`Timeslot` value(default, '15:00:00');
insert into `mydb`.`Timeslot` value(default, '15:30:00');
insert into `mydb`.`Timeslot` value(default, '16:00:00');
insert into `mydb`.`Timeslot` value(default, '16:30:00');
insert into `mydb`.`Timeslot` value(default, '17:00:00');
insert into `mydb`.`Timeslot` value(default, '17:30:00');
insert into `mydb`.`Timeslot` value(default, '18:00:00');
insert into `mydb`.`Timeslot` value(default, '18:30:00');
insert into `mydb`.`Timeslot` value(default, '19:00:00');
insert into `mydb`.`Timeslot` value(default, '19:30:00');

insert into `mydb`.`role` value(default, "ADMIN");
insert into `mydb`.`role` value(default, "MASTER");
insert into `mydb`.`role` value(default, "USER");

-- $2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu equals 1
insert into `mydb`.`user` value(default, "admin", "$2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu", 1, "admin_name", "admin_surname", 0, 0);
insert into `mydb`.`user` value(default, "user1", "$2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu", 3, "user_name1", "user_surname1", 1000, 0);
insert into `mydb`.`user` value(default, "user2", "$2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu", 3, "user_name2", "user_surname2", 500, 0);
insert into `mydb`.`user` value(default, "master1", "$2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu", 2, "master_name1", "master_surname1", 0, 87);
insert into `mydb`.`user` value(default, "master2", "$2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu", 2, "master_name2", "master_surname2", 0, 72);
insert into `mydb`.`user` value(default, "master3", "$2a$12$niIVAS7qCPP/9JicT6fHEOPIdeCYc1xDTRqgb/LDl7FVNvGg81QXu", 2, "master_name3", "master_surname3", 0, 95);

insert into `mydb`.`service` value(default, "Haircut", 100);
insert into `mydb`.`service` value(default, "Manicure", 200);
insert into `mydb`.`service` value(default, "Pedicure", 300);
insert into `mydb`.`service` value(default, "Massage", 600);

insert into `mydb`.`user_service` value(default, 4, 1);
insert into `mydb`.`user_service` value(default, 4, 2);
insert into `mydb`.`user_service` value(default, 5, 2);
insert into `mydb`.`user_service` value(default, 5, 3);
insert into `mydb`.`user_service` value(default, 6, 4);