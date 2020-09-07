package com.internet.shop.service;

import com.internet.shop.model.Order;
import com.internet.shop.model.ShoppingCart;
import java.util.List;

public interface OrderService {
    Order completeOrder(ShoppingCart shoppingCart);

    Order get(Long id);

    List<Order> getUsersOrders(Long userId);

    List<Order> getAll();

    boolean deleteById(Long id);

    boolean delete(Order order);
}
