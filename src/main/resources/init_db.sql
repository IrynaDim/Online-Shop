CREATE SCHEMA `internet_shop` DEFAULT CHARACTER SET utf8;

CREATE TABLE `internet_shop`.`products`
(
    `product_id` BIGINT          NOT NULL AUTO_INCREMENT,
    `type`       VARCHAR(256)    NOT NULL,
    `price`      DOUBLE ZEROFILL NOT NULL,
    `deleted`    TINYINT         NULL DEFAULT 0,
    PRIMARY KEY (`product_id`),
    UNIQUE INDEX `id_UNIQUE` (`product_id` ASC) VISIBLE,
);

CREATE TABLE `internet_shop`.`users` (
  `user_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(226) NOT NULL,
  `password` VARCHAR(226) NOT NULL,
  `deleted`    TINYINT         NULL DEFAULT 0,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `internet_shop`.`users`
ADD UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE;
;

CREATE TABLE `internet_shop`.`users_roles` (
  `user_id` BIGINT(11) NOT NULL,
  `role_id` BIGINT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `internet_shop`.`roles` (
  `role_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE INDEX `role_name_UNIQUE` (`role_name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

INSERT INTO `internet_shop`.`roles` (`role_id`, `role_name`) VALUES ('1', 'ADMIN');
INSERT INTO `internet_shop`.`roles` (`role_id`, `role_name`) VALUES ('2', 'USER');

ALTER TABLE `internet_shop`.`users_roles`
ADD INDEX `user_role_idx` (`role_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`users_roles`
ADD CONSTRAINT `user_role`
  FOREIGN KEY (`role_id`)
  REFERENCES `internet_shop`.`roles` (`role_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`users_role`
ADD INDEX `user_id_idx` (`user_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`users_role`
ADD CONSTRAINT `user_id`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet_shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`users_role`
RENAME TO  `internet_shop`.`user_roles` ;

CREATE TABLE `internet_shop`.`orders` (
  `order_id` BIGINT(11) NOT NULL,
  `user_id` VARCHAR(256) NOT NULL,
  `deleted` TINYINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`order_id`),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `internet_shop`.`orders`
CHANGE COLUMN `user_id` `user_id` BIGINT(11) NOT NULL ;

CREATE TABLE `internet_shop`.`orders_products` (
  `order_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `internet_shop`.`orders_products`
ADD INDEX `orders_idx` (`order_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`orders_products`
ADD CONSTRAINT `orders`
  FOREIGN KEY (`order_id`)
  REFERENCES `internet_shop`.`orders` (`order_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `internet_shop`.`orders`
ADD CONSTRAINT `user`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet_shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`orders`
CHANGE COLUMN `order_id` `order_id` BIGINT NOT NULL AUTO_INCREMENT ;

CREATE TABLE `internet_shop`.`shopping_carts` (
  `cart_id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(11) NOT NULL,
  PRIMARY KEY (`cart_id`),
  UNIQUE INDEX `cart_id_UNIQUE` (`cart_id` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `internet_shop`.`shopping_carts`
ADD INDEX `user_id_idx` (`user_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`shopping_carts`
ADD CONSTRAINT `user_id`
  FOREIGN KEY (`user_id`)
  REFERENCES `internet_shop`.`users` (`user_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  CREATE TABLE `internet_shop`.`shopping_carts_products` (
  `cart_id` BIGINT(11) NOT NULL,
  `product_id` BIGINT(11) NOT NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD INDEX `shopping_cart_idx` (`cart_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD CONSTRAINT `shopping_cart`
  FOREIGN KEY (`cart_id`)
  REFERENCES `internet_shop`.`shopping_carts` (`cart_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD INDEX `product_idx` (`product_id` ASC) VISIBLE;
;
ALTER TABLE `internet_shop`.`shopping_carts_products`
ADD CONSTRAINT `product`
  FOREIGN KEY (`product_id`)
  REFERENCES `internet_shop`.`products` (`product_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `internet_shop`.`shopping_carts`
ADD COLUMN `deleted` TINYINT NOT NULL DEFAULT '0' AFTER `user_id`;



