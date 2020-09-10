package com.internet.shop.controllers;

import com.internet.shop.lib.Injector;
import com.internet.shop.model.Product;
import com.internet.shop.service.ProductService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddProductToCartController extends HttpServlet {
    public static final Injector injector = Injector.getInstance("com.internet.shop");
    private ProductService productService = (ProductService) injector
            .getInstance(ProductService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/products/add.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        String price = req.getParameter("price");
        if (!price.matches("[0-9.]+")) {
            req.setAttribute("message", "You put incorrect price. "
                    + "Please use only numbers separated by one dot.");
            req.getRequestDispatcher("/WEB-INF/views/products/add.jsp").forward(req, resp);
        }
        Double priceDouble = Double.parseDouble(price);
        productService.create(new Product(name, priceDouble));
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
