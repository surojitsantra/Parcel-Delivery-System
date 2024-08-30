package com.delivery.service;

import com.delivery.model.Customer;
import com.delivery.model.Item;
import com.delivery.model.Order;
import com.delivery.model.OrderStatus;
import lombok.Data;

import java.util.*;
import java.util.logging.Logger;

@Data
public class OrderManager {
    final Logger LOG = Logger.getLogger(OrderManager.class.getName());

    private Map<String, Order> orders;

    private static Integer orderIdCounter = 0;
    private NotificationService notificationService;

    public OrderManager(NotificationService notificationService) {
        this.orders = new HashMap<>();
        this.notificationService = notificationService;
    }

    public Order placeOrder(Customer from, Customer to, Item item) {
        Order order = new Order();
        order.setOrderId(String.valueOf(++orderIdCounter));
        order.setCustomerFrom(from);
        order.setCustomerTo(to);
        order.setItem(item);
        order.setAssignedDriver(null);
        order.setStatus(OrderStatus.UN_ASSIGNED);

        orders.put(order.getOrderId(), order);

        LOG.info("Order id : " +order.getOrderId());
        notificationService.notify(from, "order is placed by you Order id : " +order.getOrderId());
        notificationService.notify(to, "order is placed for you Order id : " +order.getOrderId());

        return order;
    }

}
