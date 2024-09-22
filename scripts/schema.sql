CREATE TABLE `insider`.`message` (
                                     `id` INT NOT NULL AUTO_INCREMENT,
                                     `message_content` NVARCHAR(250) NOT NULL,
                                     `external_message_id` NVARCHAR(36),
                                     `phone_number` NVARCHAR(15) NOT NULL,
                                     `status` NVARCHAR(15) NOT NULL,
                                     PRIMARY KEY (`id`));
