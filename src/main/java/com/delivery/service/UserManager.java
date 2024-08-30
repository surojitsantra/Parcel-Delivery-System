package com.delivery.service;

import com.delivery.model.Customer;
import com.delivery.model.Driver;
import com.delivery.model.DriverStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Data
public class UserManager {
    final Logger LOG = Logger.getLogger(UserManager.class.getName());
    private static Integer customerIdCounter = 0;
    private static Integer driverIdCounter = 0;
    private List<Customer> customers;
    private List<Driver> drivers;
    private NotificationService notificationService;

    public UserManager(NotificationService notificationService) {
        this.customers = new ArrayList<>();
        this.drivers = new ArrayList<>();
        this.notificationService = notificationService;
    }

    public void onboardCustomer(String name, String email, String phone, String address) {
        Customer customer = new Customer();
        customer.setId(String.valueOf(++customerIdCounter));
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);

        customers.add(customer);
        notificationService.notify(customer, "welcome " +name +"!");

    }

    public void onboardDriver(String name, String phone, String email) {
        Driver driver = new Driver();
        driver.setId(String.valueOf(++driverIdCounter));
        driver.setName(name);
        driver.setPhone(phone);
        driver.setEmail(email);
        driver.setStatus(DriverStatus.IDLE);

        drivers.add(driver);

        notificationService.notify(driver, "welcome " +name +"!");

    }



}
