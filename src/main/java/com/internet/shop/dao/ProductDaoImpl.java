package com.internet.shop.dao;

import com.internet.shop.db.Storage;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import java.util.List;
import java.util.Optional;

@Dao
public class ProductDaoImpl implements ProductDao {

    @Override
    public Product create(Product product) {
        Storage.addProduct(product);
        return product;
    }

    @Override
    public Optional<Product> getById(Long productId) {
        for (int i = 0; i < Storage.products.size(); i++) {
            if (Storage.products.get(i).getId() == productId) {
                return Optional.ofNullable(Storage.products.get(i));
            }
        }
        return Optional.empty();
    }

    @Override
    public Product update(Product product) {
        for (int i = 0; i < Storage.products.size(); i++) {
            if (Storage.products.get(i).getId() == product.getId()) {
                Storage.products.set(i, product);
            }
        }
        return product;
    }

    @Override
    public boolean deleteById(Long productId) {
        for (int i = 0; i < Storage.products.size(); i++) {
            if (Storage.products.get(i).getId() == productId) {
                Storage.products.remove(i);
            }
        }
        return true;
    }

    @Override
    public boolean delete(Product product) {
        for (int i = 0; i < Storage.products.size(); i++) {
            if (Storage.products.get(i).equals(product)) {
                Storage.products.remove(i);
            }
        }
        return true;
    }

    @Override
    public List<Product> getAllProducts() {
        return Storage.products;
    }
}
