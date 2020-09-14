package com.internet.shop.dao;

import com.internet.shop.model.User;
import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    boolean delete(User user);

    Optional<User> findByLogin(String login);
}

