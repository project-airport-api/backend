CREATE DATABASE IF NOT EXISTS airport;

USE airport;

DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS `user`
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nickname    VARCHAR(256)  DEFAULT ''                NOT NULL,
    username    VARCHAR(256)  DEFAULT ''                NOT NULL,
    role        VARCHAR(10)   DEFAULT 'user'            NOT NULL, -- user or admin
    password    VARCHAR(256)  DEFAULT ''                NOT NULL,
    avatar      VARCHAR(1024) DEFAULT ''                NOT NULL,
    create_time DATETIME      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time DATETIME      DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    is_deleted  TINYINT       DEFAULT 0                           -- Soft deletion: 0: not deleted; 1: deleted
);

DROP TABLE IF EXISTS `api`;

CREATE TABLE IF NOT EXISTS `api`
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(256)  DEFAULT ''                NOT NULL,
    description     VARCHAR(1024) DEFAULT ''                NOT NULL,
    url             VARCHAR(1024) DEFAULT ''                NOT NULL,
    request_method  VARCHAR(10)   DEFAULT 'GET'             NOT NULL,
    request_header  TEXT          DEFAULT NULL,
    response_header TEXT          DEFAULT NULL,
    is_enabled      TINYINT       DEFAULT 0                 NOT NULL, -- 0: disabled; 1: enabled
    created_by      BIGINT                                  NOT NULL, -- User ID
    create_time     DATETIME      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_time     DATETIME      DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP,
    is_deleted      TINYINT       DEFAULT 0
);

INSERT INTO `user` (nickname, username, role, password, avatar)
VALUES ('Admin User', 'admin', 'admin',
        '$2a$10$NWSjAdOBKuy1r3xSsyIRB.X834PYZs2qRoTY3A7BequUFU3kg3HKy',
        'https://avatar.iran.liara.run/username?username=admin');


INSERT INTO `user` (nickname, username, role, password, avatar)
VALUES ('Regular User', 'user', 'user',
        '$2a$10$NWSjAdOBKuy1r3xSsyIRB.X834PYZs2qRoTY3A7BequUFU3kg3HKy',
        'https://avatar.iran.liara.run/username?username=user');