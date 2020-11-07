CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `internet_shop`.`products` (
  `product_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(226) NOT NULL,
  `price` VARCHAR(226) NOT NULL,
  `salt` VARBINARY(16) NOT NULL,
  `deleted` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`product_id`),
  UNIQUE INDEX `product_id_UNIQUE` (`product_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `internet_shop`.`users` (
  `user_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(256) NOT NULL,
  `password` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `internet_shop`.`orders` (
  `order_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(256) NOT NULL,
  `deleted` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`order_id`),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `internet_shop`.`users`
ADD COLUMN `deleted` TINYINT NOT NULL AFTER `password`;

ALTER TABLE `internet_shop`.`users`
CHANGE COLUMN `deleted` `deleted` TINYINT NOT NULL DEFAULT '0' ;

ALTER TABLE `internet_shop`.`orders`
CHANGE COLUMN `user_id` `user_id` BIGINT(11) NOT NULL ;

CREATE TABLE `internet_shop`.`roles` (
  `role_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `role_id_UNIQUE` (`role_id` ASC) VISIBLE,
  UNIQUE INDEX `role_name_UNIQUE` (`role_name` ASC) VISIBLE);

CREATE TABLE `internet_shop`.`users_roles` (
  `user_id` BIGINT(11) NOT NULL,
  `role_id` BIGINT(11) NOT NULL);

ALTER TABLE `internet_shop`.`users_roles`
ADD INDEX `user_id_fk_idx` (`user_id` ASC) VISIBLE,
ADD INDEX `role_id_fk_idx` (`role_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`users_roles`
ADD CONSTRAINT `user_id_fk`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet_shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `role_id_fk`
  FOREIGN KEY (`role_id`)
  REFERENCES `internet_shop`.`roles` (`role_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`orders`
ADD INDEX `user_id_idx` (`user_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`orders`
ADD CONSTRAINT `user_id`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet_shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `internet_shop`.`shopping_carts` (
  `cart_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(256) NOT NULL,
  `deleted` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`cart_id`),
  UNIQUE INDEX `cart_id_UNIQUE` (`cart_id` ASC) VISIBLE);

ALTER TABLE `internet_shop`.`shopping_carts`
CHANGE COLUMN `user_id` `user_id` BIGINT(11) NOT NULL ;

ALTER TABLE `internet_shop`.`shopping_carts`
CHANGE COLUMN `user_id` `user_id` BIGINT NOT NULL ;

ALTER TABLE `internet_shop`.`shopping_carts`
ADD INDEX `user_id_fk_sc_idx` (`user_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`shopping_carts`
ADD CONSTRAINT `user_id_fk_sc`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet_shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `internet_shop`.`shopping_carts_products` (
  `cart_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL);

ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD INDEX `cart_id_fk_idx` (`cart_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD CONSTRAINT `cart_id_fk`
  FOREIGN KEY (`cart_id`)
  REFERENCES `internet_shop`.`shopping_carts` (`cart_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD INDEX `product_id_fk_idx` (`product_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD CONSTRAINT `product_id_fk`
  FOREIGN KEY (`product_id`)
  REFERENCES `internet_shop`.`products` (`product_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `internet_shop`.`orders_products` (
  `order_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL);

ALTER TABLE `internet_shop`.`orders_products`
ADD INDEX `fk_order_id_idx` (`order_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`orders_products`
ADD CONSTRAINT `fk_order_id`
  FOREIGN KEY (`order_id`)
  REFERENCES `internet_shop`.`orders` (`order_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`orders_products`
ADD INDEX `fk_productid_idx` (`product_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`orders_products`
ADD CONSTRAINT `fk_productid`
  FOREIGN KEY (`product_id`)
  REFERENCES `internet_shop`.`products` (`product_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

INSERT INTO `internet_shop`.`roles` (`role_id`, `role_name`) VALUES ('1', 'ADMIN');
INSERT INTO `internet_shop`.`roles` (`role_id`, `role_name`) VALUES ('2', 'USER');
