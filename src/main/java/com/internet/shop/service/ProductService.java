package com.internet.shop.service;

import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import java.util.List;

@Dao
public interface ProductService {
    Product create(Product product);

    Product get(Long id);

    Product update(Product product);

    boolean delete(Long productId);

    List<Product> getAllProducts();

}