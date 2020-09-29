package com.internet.shop.dao.impl;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.db.Storage;
import com.internet.shop.model.ShoppingCart;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class ShoppingCartDaoImpl implements ShoppingCartDao {

    @Override
    public ShoppingCart create(ShoppingCart cart) {
        Storage.addShoppingCart(cart);
        return cart;
    }

    @Override
    public Optional<ShoppingCart> get(Long id) {
        return Storage.shoppingCarts.stream()
                .filter(cart -> cart.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<ShoppingCart> getAll() {
        return Storage.shoppingCarts;
    }

    @Override
    public ShoppingCart update(ShoppingCart cart) {
        IntStream.range(0, Storage.shoppingCarts.size())
                .filter(i -> Storage.shoppingCarts.get(i).getId().equals(cart.getId()))
                .forEach(i -> Storage.shoppingCarts.set(i, cart));
        return cart;
    }

    @Override
    public boolean deleteById(Long id) {
        return Storage.shoppingCarts.removeIf(cart -> (cart.getId().equals(id)));
    }

    @Override
    public boolean delete(Long id) {
        return Storage.shoppingCarts.removeIf(cart -> (cart.getId().equals(id)));
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        return Storage.shoppingCarts.stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst();
    }
}
