package com.internet.shop.dao.jdbs;

import com.internet.shop.dao.OrderDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Order;
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
public class OrderDaoJdbcImpl implements OrderDao {

    @Override
    public List<Order> getUserOrders(Long userId) {
        List<Order> userOrders = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders WHERE user_id = ? AND deleted = FALSE");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userOrders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Finding orders by user id: "
                    + userId + "was failed. ", e);
        }
        for (Order order : userOrders) {
            order.setProducts(getOrderProducts(order.getOrderId()));
        }
        return userOrders;
    }

    @Override
    public Order create(Order order) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO orders (user_id) VALUES (?)",
                            Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, order.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setOrderId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Creating order: " + order + "was failed. ", e);
        }
        addProducts(order.getProducts(), order.getOrderId());
        return order;
    }

    @Override
    public Optional<Order> get(Long orderId) {
        String query = "SELECT * FROM orders WHERE order_id = ? AND deleted = FALSE";
        List<Product> productsInOrder = getOrderProducts(orderId);
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Order searchedOrder = getOrderFromResultSet(resultSet);
                searchedOrder.setProducts(productsInOrder);
                return Optional.of(searchedOrder);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Finding the order by id: "
                    + orderId + "was failed. ", e);
        }
    }

    @Override
    public Order update(Order order) {
        deleteProducts(order.getOrderId());
        addProducts(order.getProducts(), order.getOrderId());
        return order;
    }

    @Override
    public boolean deleteById(Long orderId) {
        String query = "UPDATE orders SET deleted = TRUE WHERE order_id = ? AND deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting the order by id: "
                    + orderId + "was failed. ", e);
        }
    }

    @Override
    public List<Order> getAll() {
        List<Order> allOrders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                allOrders.add(getOrderFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all orders from DB was failed", e);
        }
        for (Order order : allOrders) {
            order.setProducts(getOrderProducts(order.getOrderId()));
        }
        return allOrders;
    }

    private void deleteProducts(long orderId) {
        String query = "DELETE FROM orders_products WHERE order_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting products by order id: "
                    + orderId + " was failed. ", e);
        }
    }

    private Order getOrderFromResultSet(ResultSet resultSet) throws SQLException {
        Long orderId = resultSet.getLong("order_id");
        Long userId = resultSet.getLong("user_id");
        Order order = new Order(userId);
        order.setOrderId(orderId);
        return order;
    }

    private List<Product> getOrderProducts(long orderId) {
        List<Product> orderedProducts = new ArrayList<>();
        String query = "SELECT product_id, type, price FROM products "
                + "JOIN orders_products USING (product_id) "
                + "WHERE order_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, orderId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("product_id");
                String productName = resultSet.getString("type");
                double price = resultSet.getDouble("price");
                Product productFromOrder = new Product(productName, price);
                productFromOrder.setId(id);
                orderedProducts.add(productFromOrder);
            }
            return orderedProducts;
        } catch (SQLException e) {
            throw new DataProcessingException("Getting products from DB "
                    + "by order id:" + orderId + " was failed. ", e);
        }
    }

    private void addProducts(List<Product> products, long orderId) {
        String query = "INSERT INTO orders_products (order_id, product_id) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Product product : products) {
                statement.setLong(1, orderId);
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Adding products to order by order id: "
                    + orderId + " was failed. ", e);
        }
    }
}
