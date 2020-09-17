package com.internet.shop.controllers.data;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import java.io.IOException;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectDataController extends HttpServlet {
    public static final Injector injector = Injector.getInstance("com.internet.shop");
    private UserService userService = (UserService) injector.getInstance(UserService.class);
    private ShoppingCartService shoppingCart = (ShoppingCartService) injector
            .getInstance(ShoppingCartService.class);
    private ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User bob = new User("Bob", "1111");
        bob.setRoles(Set.of(Role.of("USER")));
        userService.create(bob);
        shoppingCart.create(new ShoppingCart(bob.getId()));

        User maria = new User("Maria", "0000");
        maria.setRoles(Set.of(Role.of("USER")));
        userService.create(maria);
        shoppingCart.create(new ShoppingCart(maria.getId()));

        User admin = new User("Admin", "1557");
        admin.setRoles(Set.of(Role.of("ADMIN")));
        userService.create(admin);

        Product teapot = new Product("Teapot", 300);
        Product spoon = new Product("Spoon", 50);
        productService.create(teapot);
        productService.create(spoon);
        req.getRequestDispatcher("/WEB-INF/views/injectData.jsp").forward(req, resp);
    }
}
