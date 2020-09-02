package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;
import java.math.BigDecimal;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        Product product = new Product("Teapot", new BigDecimal(320.0));
        Product product1 = new Product("Cup", new BigDecimal(80.0));

        productService.create(product);
        productService.create(product1);
        productService.create(new Product("Spoon", new BigDecimal(25.0)));
        productService.create(new Product("Fork", new BigDecimal(35.0)));
        productService.create(new Product("Knife", new BigDecimal(50.0)));
        productService.delete(product);

        productService.get(4L);

        product1.setPrice(new BigDecimal(70.0));
        productService.update(product1);

        productService.deleteById(3L);
        productService.deleteById(0L);

        System.out.println(productService.getAllProducts());
    }
}
