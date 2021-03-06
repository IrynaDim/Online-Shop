package com.internet.shop.dao;

import com.internet.shop.model.ShoppingCart;
import java.util.Optional;

public interface ShoppingCartDao extends GenericDao<ShoppingCart, Long> {

    boolean delete(Long id);

    Optional<ShoppingCart> getByUserId(Long userId);
}
