package com.internet.shop.dao.jdbs;

import com.internet.shop.dao.UserDao;
import com.internet.shop.exceptions.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserDaoJdbcImpl implements UserDao {

    @Override
    public Optional<User> findByLogin(String login) {
        User user = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE deleted=FALSE AND login=?");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = getFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user with login "
                    + login + " was failed. ", e);
        }
        user.setRoles(getUserRoles(user.getId()));
        return Optional.of(user);
    }

    @Override
    public User create(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO users (login, password) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Adding user with id "
                    + user.getId() + " was failed. ", e);
        }
        setUserRole(user);
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        User user = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE deleted=FALSE AND user_id=?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = Optional.of(getFromResultSet(resultSet)).get();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user with id " + id + " was failed. ", e);
        }
        if (user != null) {
            user.setRoles(getUserRoles(id));
        }
        return Optional.of(user);
    }

    @Override
    public User update(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET login = ?, password = ? WHERE user_id = ?");
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Updating product with id "
                    + user.getId() + " was failed. ", e);
        }
        deleteRoles(user.getId());
        setUserRole(user);
        return user;
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET deleted = TRUE WHERE user_id = ?");
            statement.setLong(1, id);
            int isDeleted = statement.executeUpdate();
            return isDeleted == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Removing user with id " + id + " was failed. ", e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        User user = null;
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE deleted = FALSE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = getFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all users was failed. ", e);
        }
        for (User userInArray : users) {
            userInArray.setRoles(getUserRoles(user.getId()));
        }
        return users;
    }

    private User getFromResultSet(ResultSet resultSet) throws SQLException {
        long userId = resultSet.getLong("user_id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        User user = new User(login, password);
        user.setId(userId);
        return user;
    }

    private void setUserRole(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            for (Role role : user.getRoles()) {
                String roleName = String.valueOf(role.getRoleName());
                // long roleId = getRoleId(roleName);
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO users_roles (user_id, role_id) VALUES (?, "
                                + "(SELECT role_id FROM roles WHERE role_name = ?));");
                statement.setLong(1, user.getId());
                statement.setString(2, roleName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Setting role for user with id  "
                    + user.getId() + " was failed. ", e);
        }
    }

    private void deleteRoles(long userId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM users_roles WHERE user_id = ?");
            statement.setLong(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Deleting roles for user with id: "
                    + userId + " was failed. ", e);
        }
    }

    private Set<Role> getUserRoles(long userId) {
        Set<Role> roles = new HashSet<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM roles INNER JOIN users_roles "
                            + "ON roles.role_id = users_roles.role_id "
                            + "WHERE users_roles.user_id = ?;");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Role role = Role.of(resultSet.getString("role_name"));
                role.setId(resultSet.getLong("role_id"));
                roles.add(role);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting roles for user with id: "
                    + userId + " was failed. ", e);
        }
        return roles;
    }
}
