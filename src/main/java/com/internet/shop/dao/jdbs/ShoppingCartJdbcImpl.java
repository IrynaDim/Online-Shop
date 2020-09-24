package com.internet.shop.dao.jdbs;

import com.internet.shop.dao.ShoppingCartDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Product;
import com.internet.shop.model.ShoppingCart;
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
public class ShoppingCartJdbcImpl implements ShoppingCartDao {
    @Override
    public boolean delete(ShoppingCart cart) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE shopping_carts SET deleted = TRUE WHERE cart_id = ? "
                            + "AND deleted = FALSE");
            statement.setLong(1, cart.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting order by id: "
                    + cart.getId() + " was failed", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        deleteProducts(id);
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE shopping_carts SET deleted = TRUE WHERE cart_id = ? "
                            + "AND deleted = FALSE");
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting order by id: "
                    + id + " was failed", e);
        }
    }

    @Override
    public Optional<ShoppingCart> getByUserId(Long userId) {
        Optional<ShoppingCart> cart = Optional.empty();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM shopping_carts WHERE user_id = ? AND deleted = FALSE");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                cart = Optional.of(getCartFromResult(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Finding cart by user id: "
                    + userId + " was failed", e);
        }
        if (cart.isEmpty()) {
            return cart;
        }
        cart.get().setProducts(getProducts(cart.get().getId()));
        return cart;
    }

    @Override
    public ShoppingCart create(ShoppingCart cart) {
        String query = "INSERT INTO shopping_carts (user_id) VALUES (?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, cart.getUserId());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                cart.setId(resultSet.getLong(1));
            }
            return cart;
        } catch (SQLException e) {
            throw new DataProcessingException("Creating cart " + cart + " was failed", e);
        }
    }

    @Override
    public Optional<ShoppingCart> get(Long cartId) {
        String query = "SELECT * FROM shopping_carts WHERE cart_id = ? AND deleted = FALSE";
        List<Product> productsInCart = getProducts(cartId);
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart searchedCart = getCartFromResult(resultSet);
                searchedCart.setProducts(productsInCart);
                return Optional.of(searchedCart);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DataProcessingException("Finding cart by id: "
                    + cartId + " was failed", e);
        }
    }

    @Override
    public ShoppingCart update(ShoppingCart cart) {
        deleteProducts(cart.getId());
        addProducts(cart.getProducts(), cart.getId());
        return cart;
    }

    @Override
    public boolean deleteById(Long cartId) {
        String query = "UPDATE shopping_carts SET deleted = TRUE WHERE cart_id = ? "
                + "AND deleted = FALSE";
        deleteProducts(cartId);
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, cartId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting cart by id: "
                    + cartId + " was failed", e);
        }
    }

    @Override
    public List<ShoppingCart> getAll() {
        List<ShoppingCart> allCarts = new ArrayList<>();
        String query = "SELECT * FROM shopping_carts WHERE deleted = FALSE";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                allCarts.add(getCartFromResult(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting carts from DB was failed", e);
        }
        for (ShoppingCart cart : allCarts) {
            cart.setProducts(getProducts(cart.getId()));
        }
        return allCarts;
    }

    private void deleteProducts(long cartId) {
        String query = "DELETE FROM shopping_carts_products WHERE cart_id = ?";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, cartId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting products by cart id: "
                    + cartId + " was failed", e);
        }
    }

    private List<Product> getProducts(long cartId) {
        List<Product> cartProducts = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT product_id, type, price FROM products "
                            + "JOIN shopping_carts_products USING (product_id) "
                            + "WHERE cart_id = ?");
            statement.setLong(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("product_id");
                String type = resultSet.getString("type");
                double price = resultSet.getDouble("price");
                Product productFromCart = new Product(type, price);
                productFromCart.setId(id);
                cartProducts.add(productFromCart);
            }
            return cartProducts;
        } catch (SQLException e) {
            throw new DataProcessingException("Getting products from DB "
                    + "by cart id:" + cartId + " was failed", e);
        }
    }

    private void addProducts(List<Product> products, long cartId) {
        String query = "INSERT INTO shopping_carts_products (cart_id, product_id) VALUES (?, ?)";
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(query);
            for (Product product : products) {
                statement.setLong(1, cartId);
                statement.setLong(2, product.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Adding products by cart id: "
                    + cartId + " was failed", e);
        }
    }

    private ShoppingCart getCartFromResult(ResultSet resultSet) throws SQLException {
        long cartId = resultSet.getLong("cart_id");
        long userId = resultSet.getLong("user_id");
        ShoppingCart newCart = new ShoppingCart(userId);
        newCart.setId(cartId);
        return newCart;
    }
}
