ALTER TABLE `token`
RENAME COLUMN `token_type` TO `type`;

ALTER TABLE `token`
MODIFY COLUMN `type` enum('EMAIL_ACTIVATION','PASSWORD_RESET', 'JWT') DEFAULT NULL;

ALTER TABLE `token`
ADD COLUMN `value` varchar(255) DEFAULT NULL;