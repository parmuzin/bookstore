package com.bookstore.service;

import com.bookstore.model.Order;

import java.util.List;

public interface OrderService {

    List<Order> findAllOrders();

    Order findOrderById(Long orderId);

    Order saveOrder(Order order);

    void deleteOrder(Order order);

    List<Order> findByUserName(String userName);
}
