package com.internet.shop;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;

public class Application {
    private static Injector injector = Injector.getInstance("com.internet.shop");

    public static void main(String[] args) {
        ProductService productService = (ProductService) injector.getInstance(ProductService.class);
        Product teapot = new Product("Teapot", 320.0);
        Product cup = new Product("Cup", 80.0);
        Product spoon = new Product("Spoon", 25.0);
        Product fork = new Product("Fork", 35.0);
        Product knife = new Product("Knife", 50.0);
        productService.create(teapot);
        productService.create(cup);
        productService.create(spoon);
        productService.create(fork);
        productService.create(knife);
        System.out.println("All products: \n" + productService.getAllProducts());
        System.out.println("Get product by id 1 (teapot): \n" + productService.get(teapot.getId()));
        cup.setPrice(70.0);
        productService.update(cup);
        System.out.println("Update price on cup. Now it is 70.0: \n"
                + productService.get(teapot.getId()));
        productService.delete(teapot.getId());
        productService.delete(cup.getId());
        System.out.println("All products without teapot and cup: \n"
                + productService.getAllProducts());

        UserService userService = (UserService) injector.getInstance(UserService.class);
        User user1 = new User("Ira", "Irinka668", "1256");
        User user2 = new User("Valera", "Valera", "0000");
        User user3 = new User("Olga", "Olga31", "1111");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        System.out.println("All users: \n" + userService.getAll());
        user3.setName("Anna");
        userService.update(user3);
        System.out.println("Get update user by id 3 (now its Anna): \n" + userService.get(3L));
        userService.delete(2L);
        System.out.println("All users without Valera: \n" + userService.getAll());

        OrderService orderService = (OrderService) injector.getInstance(OrderService.class);
        Order order = new Order(1L);
        Order order2 = new Order(2L);
        Order order3 = new Order(3L);
        orderService.create(order);
        orderService.create(order2);
        orderService.create(order3);
        System.out.println("All orders: \n" + orderService.getAll());
        System.out.println("Get order with order id 1: \n" + orderService.get(1L));
        System.out.println("Get order with user id 3: \n" + orderService.getUserOrders(3L));
        orderService.delete(3L);
        System.out.println("All orders after remove: \n" + orderService.getAll());

        ShoppingCartService cartService = (ShoppingCartService) injector
                .getInstance(ShoppingCartService.class);
        ShoppingCart cart1 = new ShoppingCart(1L);
        ShoppingCart cart2 = new ShoppingCart(2L);
        ShoppingCart cart3 = new ShoppingCart(3L);
        cartService.create(cart1);
        cartService.create(cart2);
        cartService.create(cart3);
        System.out.println("All carts: \n" + cartService.getAll());
        cartService.addProduct(cart1, teapot);
        cartService.addProduct(cart2, cup);
        cartService.addProduct(cart2, spoon);
        cartService.addProduct(cart3, fork);
        cartService.addProduct(cart3, knife);
        System.out.println("Cart #1 after adding products: \n" + cartService.getByUserId(1L));
        System.out.println("Cart #2 after adding products: \n" + cartService.getByUserId(2L));
        cartService.clear(cart1);
        System.out.println("Cart #1 after remove all products: \n" + cartService.getByUserId(1L));
        cartService.deleteProduct(cart2, spoon);
        System.out.println("Cart #2 after remove spoon: \n" + cartService.getByUserId(2L));
        orderService.completeOrder(cart3);
        System.out.println("New order after completed: \n" + orderService.getUserOrders(3L));
        System.out.println("Cart 3 after complete order: \n" + cartService.getByUserId(3L));
    }
}
