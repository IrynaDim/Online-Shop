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
