package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Order;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.service.OrderService;
import com.internet.shop.service.ShoppingCartService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompleteOrderController extends HttpServlet {
    public static final Injector injector = Injector
            .getInstance("com.internet.shop");
    private static final Long USER_ID = 1L;
    private OrderService orderService =
            (OrderService) injector
                    .getInstance(OrderService.class);
    private ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector
                    .getInstance(ShoppingCartService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ShoppingCart cart = shoppingCartService.getByUserId(USER_ID);
        Order order = orderService.completeOrder(cart);
        req.getRequestDispatcher("/WEB-INF/views/orders/complete.jsp")
                .forward(req, resp);
    }
}
