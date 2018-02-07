package com.bookstore.service.impl;

import com.bookstore.model.Order;
import com.bookstore.model.OrderBook;
import com.bookstore.model.User;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order findOrderById(Long orderId) {
        return orderRepository.findOne(orderId);
    }


    @Override
    public Order saveOrder(Order order) {
        for (OrderBook orderBook : order.getBooks()) {
            orderBook.setOrder(order);
            orderBook.getBook().getOrders().add(orderBook);
        }

        order.setOrderDate(Calendar.getInstance().getTime());

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public List<Order> findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        return orderRepository.findByUser(user);
    }
}
