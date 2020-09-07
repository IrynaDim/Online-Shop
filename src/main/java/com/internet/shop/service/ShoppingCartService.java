package com.internet.shop.service;

import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import java.util.List;

public interface ShoppingCartService {
    ShoppingCart create(ShoppingCart cart);

    ShoppingCart addProduct(ShoppingCart cart, Product product);

    boolean deleteProduct(ShoppingCart cart, Product product);

    void clear(ShoppingCart cart);

    ShoppingCart getByUserId(Long userId);

    List<ShoppingCart> getAll();
}
