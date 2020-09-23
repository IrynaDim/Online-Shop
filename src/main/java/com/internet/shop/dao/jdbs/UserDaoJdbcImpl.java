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
    public boolean delete(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET deleted = TRUE WHERE user_id = ?");
            statement.setLong(1, user.getId());
            int isDeleted = statement.executeUpdate();
            return isDeleted == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Removing user with id " + user.getId()
                    + " was failed. ", e);
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE deleted=FALSE AND login=?");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return Optional.of(getFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting user with login "
                    + login + " was failed. ", e);
        }
        return Optional.empty();
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

            setUserRole(user);

            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Adding user with id "
                    + user.getId() + " was failed. ", e);
        }
    }

    @Override
    public Optional<User> get(Long id) {
        User user = new User();
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
        user.setRoles(getUserRoles(id));
        return Optional.of(user);
    }

    @Override
    public User update(User user) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET login = ?, password = ? WHERE user_id = ?");
            deleteRoles(user.getId());
            setUserRole(user);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setLong(3, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Updating product with id "
                    + user.getId() + " was failed. ", e);
        }
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
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users WHERE deleted = FALSE");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(getFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting all users was failed. ", e);
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
                long roleId = getRoleId(roleName);
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO users_roles (user_id, role_id) VALUES (?, ?)");
                statement.setLong(1, user.getId());
                statement.setLong(2, roleId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Setting role for user with id  "
                    + user.getId() + " was failed. ", e);
        }
    }

    private long getRoleId(String roleName) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT role_id FROM roles WHERE role_name = ?");
            statement.setString(1, roleName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting role id for role name  "
                    + roleName + " was failed. ", e);
        }
        return 0;
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
                    "SELECT role_name FROM roles INNER JOIN users_roles "
                            + "ON roles.role_id = users_roles.role_id "
                            + "WHERE users_roles.user_id = ?;");
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Role role = Role.of(resultSet.getString("role_name"));
                roles.add(role);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Getting roles for user with id: "
                    + userId + " was failed. ", e);
        }
        return roles;
    }
}
