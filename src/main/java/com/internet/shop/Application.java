package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        Product teapot = new Product("Teapot", 320.0);
        Product cup = new Product("Cup", 80.0);

        productService.create(teapot);
        productService.create(cup);
        productService.create(new Product("Spoon", 25.0));
        productService.create(new Product("Fork", 35.0));
        productService.create(new Product("Knife", 50.0));

        productService.get(teapot.getId());

        cup.setPrice(70.0);
        productService.update(cup);

        productService.delete(teapot.getId());
        productService.delete(cup.getId());

        System.out.println(productService.getAllProducts());
    }
}
