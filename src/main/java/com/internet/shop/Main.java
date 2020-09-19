package com.internet.shop;

import com.internet.shop.dao.jdbs.ProductDaoJdbcImpl;
import com.internet.shop.model.Product;

public class Main {
    public static void main(String[] args) {
        ProductDaoJdbcImpl dao = new ProductDaoJdbcImpl();
        Product coffeeCup = new Product("Coffee cup",90);
        Product spoon = new Product(2L,"Spoon",40);
        System.out.println(dao.getAll());
        System.out.println(dao.get(1L));
        System.out.println(dao.get(5L));
        System.out.println(dao.deleteById(3L));
        System.out.println(dao.deleteById(6L));
        System.out.println(dao.create(coffeeCup));
        System.out.println(dao.update(spoon));
        System.out.println(dao.getAll());
    }
}
