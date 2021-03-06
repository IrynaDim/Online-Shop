package com.internet.shop.dao.jdbs;

import com.internet.shop.dao.ProductDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class ProductDaoJdbcImpl implements ProductDao {
    @Override
    public Product create(Product product) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO products(type, price) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setId(resultSet.getLong(1));
            }
            return product;
        } catch (SQLException e) {
            throw new DataProcessingException("Adding item with id "
                    + product.getId() + " is failed. ", e);
        }
    }

    @Override
    public Optional<Product> get(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM products WHERE deleted=FALSE AND product_id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.of(getFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Get product with id " + id + " is failed. ", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM products WHERE deleted = FALSE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(getFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all products is failed. ", e);
        }
        return products;
    }

    @Override
    public Product update(Product product) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE products SET type = ?, price = ? WHERE product_id = ?");
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setLong(3, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Updating product with id "
                    + product.getId() + " is failed. ", e);
        }
        return product;
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE products SET deleted = TRUE WHERE product_id = ?");
            statement.setLong(1, id);
            int isDeleted = statement.executeUpdate();
            return isDeleted == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Removing product with id " + id + " is failed. ", e);
        }
    }

    private Product getFromResultSet(ResultSet resultSet) throws SQLException {
        long productId = resultSet.getLong("product_id");
        String name = resultSet.getString("type");
        double price = resultSet.getDouble("price");
        Product product = new Product(productId, name, price);
        return product;
    }
}
