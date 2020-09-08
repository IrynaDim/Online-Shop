package com.internet.shop.service.impl;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
import com.internet.shop.service.ShoppingCartService;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Inject
    private ShoppingCartDao shoppingCart;

    @Override
    public ShoppingCart create(ShoppingCart cart) {
        return shoppingCart.create(cart);
    }

    @Override
    public ShoppingCart addProduct(ShoppingCart cart, Product product) {
        cart.getProducts().add(product);
        return shoppingCart.update(cart);
    }

    @Override
    public boolean deleteProduct(ShoppingCart cart, Product product) {
        boolean result = cart.getProducts().remove(product);
        shoppingCart.update(cart);
        return result;
    }

    @Override
    public void clear(ShoppingCart cart) {
        cart.getProducts().clear();
        shoppingCart.update(cart);
    }

    @Override
    public ShoppingCart getByUserId(Long userId) {
        return shoppingCart.getByUserId(userId).get();
    }

    @Override
    public List<ShoppingCart> getAll() {
        return shoppingCart.getAllItems();
    }
}
