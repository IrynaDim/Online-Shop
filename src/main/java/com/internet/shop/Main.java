package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;

public class Main {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private static ProductService dao =
            (ProductService) injector.getInstance(ProductService.class);

    public static void main(String[] args) {

        Product coffeeCup = new Product("Coffee cup", 90);
        Product spoon = new Product(2L, "Spoon", 40);
        System.out.println(dao.getAllProducts());
        System.out.println(dao.get(1L));
        System.out.println(dao.get(5L));
        System.out.println(dao.delete(6L));
        System.out.println(dao.delete(6L));
        System.out.println(dao.create(coffeeCup));
        System.out.println(dao.update(spoon));
        System.out.println(dao.get(5L));
    }
}
