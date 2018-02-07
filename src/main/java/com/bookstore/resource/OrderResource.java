package com.bookstore.resource;


import com.bookstore.model.Order;
import com.bookstore.security.SecurityConstants;
import com.bookstore.service.OrderService;
import com.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

/**
 * REST controller for managing Order.
 */
@RestController
public class OrderResource {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderResource(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    /**
     * POST /orders : Create a new order.
     *
     * @param order the order to create
     * @return the ResponseEntity with status 201 (Created) and with body the new order,
     * or with status 400 (Bad Request) if the order has already an ID
     */
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order, Principal principal) {
        if (order.getOrderId() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        order.setUser(userService.findByUserName(principal.getName()));
        Order resultOrder = orderService.saveOrder(order);

        return new ResponseEntity<>(resultOrder, HttpStatus.CREATED);
    }

    /**
     * PUT /orders : Updates an existing order.
     *
     * @param order the order to update
     * @return the ResponseEntity with state 200 (OK) and with body the updated order,
     * or with status 400 (Bad Request) if the order is not valid
     */
    @PutMapping("/orders")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<Order> updateOrder(@RequestBody Order order/*, Principal principal*/) {
//        if (order.getOrderId() == null) {
//            return createOrder(order, principal);
//        }
        orderService.saveOrder(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * GET /orders : get all the orders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list ob orders in body,
     * or with status 204 (NO CONTENT) if there no orders in repository
     */
    @GetMapping("/orders")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.findAllOrders();
        if (orders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * GET /orders/:id : get the "id" order.
     *
     * @param id the id of the order to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the order,
     * or with status 404 (NOT FOUND)
     */
    @RequestMapping("/orders/{id}")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.findOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * DELETE /orders/:id : delete the "id" order.
     *
     * @param id the id of the order to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/orders/{id}")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<Order> deleteOrder(@PathVariable Long id) {
        Order order = orderService.findOrderById(id);
        if (order == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderService.deleteOrder(order);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/users/orders")
    @Secured(SecurityConstants.ADMIN)
    public ResponseEntity<List<Order>> getOrdersByUser(@RequestParam String userName) {
        List<Order> orders = orderService.findByUserName(userName);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/user/orders")
    public ResponseEntity<List<Order>> getOrdersByCurrentUser(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        List<Order> orders = orderService.findByUserName(principal.getName());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}