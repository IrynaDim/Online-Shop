package com.internet.shop.controllers.data;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.model.Role;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.model.User;
import com.internet.shop.service.ProductService;
import com.internet.shop.service.ShoppingCartService;
import com.internet.shop.service.UserService;
import com.internet.shop.util.HashUtil;
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
        User harry = new User("Harry", "1111");
        harry.setSalt(HashUtil.getSalt());
        harry.setRoles(Set.of(Role.of("USER")));
        userService.create(harry);
        shoppingCart.create(new ShoppingCart(harry.getId()));

        User valentina = new User("Valentina", "0000");
        valentina.setSalt(HashUtil.getSalt());
        valentina.setRoles(Set.of(Role.of("USER")));
        userService.create(valentina);
        shoppingCart.create(new ShoppingCart(valentina.getId()));

        User admin = new User("AdminIra", "1557");
        admin.setSalt(HashUtil.getSalt());
        admin.setRoles(Set.of(Role.of("ADMIN")));
        userService.create(admin);

        Product teapot = new Product("Teapot", 300);
        Product fork = new Product("Fork", 50);
        productService.create(teapot);
        productService.create(fork);
        req.getRequestDispatcher("/WEB-INF/views/injectData.jsp").forward(req, resp);
    }
}
