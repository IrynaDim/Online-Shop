package com.internet.shop.service;

import com.internet.shop.model.Order;
import com.internet.shop.model.ShoppingCart;
import java.util.List;

public interface OrderService {
    Order create(Order order);

    Order completeOrder(ShoppingCart cart);

    List<Order> getUserOrders(Long userId);

    Order get(Long id);

    List<Order> getAll();

    boolean delete(Long id);
}
