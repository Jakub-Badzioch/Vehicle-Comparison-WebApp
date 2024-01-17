CREATE TABLE role (
                      id binary(16) NOT NULL,
                      name varchar(255) DEFAULT NULL,
                      PRIMARY KEY (id)
);

CREATE TABLE `template` (
                            `id` binary(16) NOT NULL,
                            `body` tinytext,
                            `name` varchar(255) DEFAULT NULL,
                            `subject` varchar(255) DEFAULT NULL,
                            PRIMARY KEY (`id`)
);

CREATE TABLE `user` (
                        `id` binary(16) NOT NULL,
                        `email` varchar(255) DEFAULT NULL,
                        `nick_name` varchar(255) DEFAULT NULL,
                        `password` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `idx_email` (`email`)
);

CREATE TABLE `token` (
                         `id` binary(16) NOT NULL,
                         `expiration_date` datetime(6) DEFAULT NULL,
                         `token_type` enum('EMAIL_ACTIVATION','PASSWORD_RESET') DEFAULT NULL,
                         `user_id` binary(16) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `idx_user_id` (`user_id`),
                         CONSTRAINT `fk_token_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `user_role` (
                             `user_id` binary(16) NOT NULL,
                             `role_id` binary(16) NOT NULL,
                             PRIMARY KEY (`user_id`,`role_id`),
                             KEY `idx_role_id` (`role_id`),
                             CONSTRAINT `fk_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
                             CONSTRAINT `fk_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
);