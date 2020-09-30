package com.internet.shop.model;

import java.util.Set;

public class User {
    private Long id;
    private String name;
    private String login;
    private String password;
    private byte[] salt;
    private Set<Role> roles;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, String password) {
        this(login,password);
        this.name = name;
    }

    public User(Long id, String login, String password) {
        this(login,password);
        this.id = id;
    }

    public User(String name) {
        this.name = name;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + '\'' + ", login='" + login + '\''
                + ", password='" + password + '\'' + '}';
    }
}
