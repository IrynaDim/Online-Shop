package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.*;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {
    private static final Injector injector = Injector.getInstance("com.internet.shop");
    private static ProductService productService =
            (ProductService) injector.getInstance(ProductService.class);

    private static UserService userService =
            (UserService) injector.getInstance(UserService.class);

    private static OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);

    private static ShoppingCartService cart =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);


    public static void main(String[] args) {

       Product coffeeCup = new Product("Coffee cup", 90);
       Product spoon = new Product(2L, "Spoon", 40);
        Product tea = new Product(13L,"Tea", 120);
//        System.out.println(productService.getAllProducts());
//        System.out.println(productService.get(1L));
//        System.out.println(productService.get(5L));
//        System.out.println(productService.delete(6L));
//        System.out.println(productService.delete(6L));
 //System.out.println(productService.create(tea));
//        System.out.println(productService.update(spoon));
//        System.out.println(spoon.getId());
//        System.out.println(productService.get(5L));

        List<Product> pr = new ArrayList<>();
        pr.add(spoon);
        pr.add(tea);
        Order order = new Order(2L);

        ShoppingCart sc = new ShoppingCart (2L);
        sc.setProducts(pr);

      //  System.out.println(orderService.getAll());

        cart.create(sc);

    }
}
