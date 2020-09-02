package com.internet.shop.model;

import java.util.List;

public class Order {
    private Long orderId;
    private List<Product> products;
    private Long userId;

    public Order(Long orderId, List<Product> products, Long userId) {
        this.orderId = orderId;
        this.products = products;
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Order{" + "orderId="
                + orderId + ", products="
                + products + ", userId=" + userId + '}';
    }
}
