package com.delivery.service;

import com.delivery.model.*;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.util.Objects.nonNull;

@Data
public class DeliveryManager {

    private static final int DELAY = 5;
    private final Logger LOG = Logger.getLogger(DeliveryManager.class.getName());

    private UserManager userManager;
    private OrderManager orderManager;
    private NotificationService notificationService;
    private ScheduledExecutorService scheduler;

    public DeliveryManager() {
        this.notificationService = new EmailNotificationService();
        this.userManager = new UserManager(notificationService);
        this.orderManager = new OrderManager(notificationService);
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void onboardCustomer(String name, String email, String phone, String address) {
        userManager.onboardCustomer(name, email, phone, address);

    }

    public void onboardDriver(String name, String phone, String email) {
        userManager.onboardDriver(name, phone, email);

    }

    public void placeOrder(Customer from, Customer to, Item item) {
        Order order = this.orderManager.placeOrder(from, to, item);
        assignDriverToOrder(order);
    }

    private void assignDriverToOrder(Order order) {


        if(!assignDrivers(order)) {
            LOG.info("No drivers is available for order id : " + order.getOrderId());
            this.scheduler.schedule(() -> {
                if(!assignDrivers(order)) {
                    cancelOrder(order);
                }
            }, DELAY, TimeUnit.SECONDS);
        }
    }

    private void cancelOrder(Order order) {

        if (nonNull(order) && order.getStatus().equals(OrderStatus.UN_ASSIGNED)) {
            order.setStatus(OrderStatus.CANCELLED);
            LOG.info(String.format("Order %s cancelled due to driver unavailability.", order.getOrderId()));
        }
    }

    private boolean assignDrivers(Order order) {
        List<Driver> drivers = userManager.getDrivers();
        Driver availableDriver = drivers.stream().filter(driver -> driver.getStatus() == DriverStatus.IDLE).findFirst().orElse(null);

        if (nonNull(availableDriver)) {
            availableDriver.setStatus(DriverStatus.OCCUPIED);
            order.setAssignedDriver(availableDriver);
            order.setStatus(OrderStatus.ASSIGNED);

            notificationService.notify(availableDriver, "Order id : " +order.getOrderId() + " is assigned to you.");
            notificationService.notify(order.getCustomerFrom(), "Order id : " +order.getOrderId() + " is assigned to a driver, driver details : " + availableDriver.getName());
            notificationService.notify(order.getCustomerTo(), "Order id : " +order.getOrderId() + " is assigned to a driver, driver details : " + availableDriver.getName());
            return true;
        }
        return false;
    }

    public void orderPickup(String driverName, String orderId) {
        Map<String, Order> orders = orderManager.getOrders();
        Order order = orders.getOrDefault(orderId, null);
        if (nonNull(order) && nonNull(order.getAssignedDriver())
                && order.getAssignedDriver().getName().equalsIgnoreCase(driverName)
                && order.getStatus().equals(OrderStatus.ASSIGNED)) {

            order.setStatus(OrderStatus.PICKED_UP);
            LOG.info(order.getOrderId() + " is picked up by the " + driverName);
            notificationService.notify(order.getCustomerFrom(), String.format("%s is picked up by the driver1", order.getOrderId()));
            notificationService.notify(order.getCustomerTo(), String.format("%s is picked up by the driver1", order.getOrderId()));
        } else {
            LOG.info(String.format("Order id %s cannot be picked up", order.getOrderId()));
        }
    }

    public void orderDelivered(String driverName, String orderId) {
        Map<String, Order> orders = orderManager.getOrders();
        Order order = orders.getOrDefault(orderId, null);

        if (nonNull(order) && nonNull(order.getAssignedDriver())
                && order.getAssignedDriver().getName().equalsIgnoreCase(driverName)
                && order.getStatus().equals(OrderStatus.PICKED_UP)) {
            order.setStatus(OrderStatus.DELIVERED);
            order.getAssignedDriver().setStatus(DriverStatus.IDLE);
            LOG.info(String.format("Order id %s has been delivered up by the %s", order.getOrderId(), order.getAssignedDriver().getName()));
        }

    }

    public DriverStatus driverStatus(String driverName) {
        List<Driver> drivers = userManager.getDrivers();
        for (Driver driver : drivers) {
            if (driver.getName().equalsIgnoreCase(driverName)) {
                LOG.info(String.format("Driver name %s status %s", driver.getName(), driver.getStatus()));
                return driver.getStatus();
            }
        }

        return null;
    }

    public OrderStatus orderStatus(String orderId) {
        Map<String, Order> orders = orderManager.getOrders();
        Order order = orders.getOrDefault(orderId, null);

        if (nonNull(order)) {
            LOG.info(String.format("Order id %s status %s", order.getOrderId(), order.getStatus()));
            return order.getStatus();
        }
        return null;
    }

}
