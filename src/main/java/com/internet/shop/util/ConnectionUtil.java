package com.internet.shop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver are not installed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user","root");
        connectionProps.put("password", "1234");
        String url = "jdbc:mysql://localhost:3306/internet_shop?serverTimezone=UTC";
        try {
            conn = DriverManager.getConnection(url, connectionProps);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }
}
