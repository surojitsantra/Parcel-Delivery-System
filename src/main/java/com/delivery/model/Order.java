package com.delivery.model;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private Customer customerFrom;
    private Customer customerTo;
    private Item item;
    private Driver assignedDriver;
    private OrderStatus status;
}
