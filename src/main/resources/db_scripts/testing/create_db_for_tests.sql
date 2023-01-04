SET
    @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET
    @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET
    @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

drop schema `test`;
CREATE SCHEMA IF NOT EXISTS `test` DEFAULT CHARACTER SET utf8;
USE
    `test`;


CREATE TABLE IF NOT EXISTS `test`.`Role`
(
    `id`
        int
                    NOT
                        NULL
        AUTO_INCREMENT,
    `name`
        varchar(45) NOT NULL,
    PRIMARY KEY
        (
         `id`
            )
)
    ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `test`.`User`
(
    `id`
               int
                           NOT
                               NULL
        AUTO_INCREMENT,
    `email`
               VARCHAR(45) NOT NULL unique,
    `password` VARCHAR(80) NOT NULL,
    `role_id`  int         NOT NULL,
    `name`     VARCHAR(45) NOT NULL,
    `surname`  VARCHAR(45) NOT NULL,
    `money`    bigint,
    `rating`   int,
    CONSTRAINT `role_id`
        FOREIGN KEY
            (
             `role_id`
                )
            REFERENCES `test`.`Role`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    PRIMARY KEY
        (
         `id`
            )
)
    ENGINE = InnoDB;



CREATE TABLE IF NOT EXISTS `test`.`User_Service`
(
    `id`
        int
        NOT
            NULL
        AUTO_INCREMENT,
    `user_id`
        int
        NOT
            NULL,
    `service_id`
        int
        NOT
            NULL,
    INDEX
        `master_id_idx`
        (
         `user_id`
         ASC
            ),
    INDEX `service_id_idx`
        (
         `service_id` ASC
            ),
    CONSTRAINT `master_id`
        FOREIGN KEY
            (
             `user_id`
                )
            REFERENCES `test`.`User`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `service_id`
        FOREIGN KEY
            (
             `service_id`
                )
            REFERENCES `test`.`Service`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    PRIMARY KEY
        (
         `id`
            )
)
    ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `test`.`Service`
(
    `id`
            int
                        NOT
                            NULL
        AUTO_INCREMENT,
    `name`
            VARCHAR(45) NOT NULL,
    `price` INT         NOT NULL,
    PRIMARY KEY
        (
         `id`
            )
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test`.`Timeslot`
(
    `id`
        int
        NOT
            NULL
        AUTO_INCREMENT,
    `time`
        time
        NOT
            NULL,
    PRIMARY
        KEY
        (
         `id`
            )
)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `test`.`Request`
(
    `id`
              int
                          NOT
                              NULL
        AUTO_INCREMENT,
    `date`
              date
                          not
                              null,
    `timeslot_id`
              int
                          NOT
                              NULL,
    `user_id`
              int
                          NOT
                              NULL,
    `master_id`
              int
                          NOT
                              NULL,
    `service_id`
              int
                          NOT
                              NULL,
    `status`
              varchar(45) NOT NULL,
    `version` int         NOT NULL,
    PRIMARY KEY
        (
         `id`
            ),
    INDEX `user_id_idx`
        (
         `user_id` ASC
            ),
    INDEX `service_id_idx`
        (
         `service_id` ASC
            ),
    CONSTRAINT `user_id`
        FOREIGN KEY
            (
             `user_id`
                )
            REFERENCES `test`.`User`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `timeslot_id`
        FOREIGN KEY
            (
             `timeslot_id`
                )
            REFERENCES `test`.`Timeslot`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `request_master_id`
        FOREIGN KEY
            (
             `master_id`
                )
            REFERENCES `test`.`User`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `request_service_id`
        FOREIGN KEY
            (
             `service_id`
                )
            REFERENCES `test`.`Service`
                (
                 `id`
                    )
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

