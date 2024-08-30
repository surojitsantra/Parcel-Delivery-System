package com.delivery;

import com.delivery.model.Customer;
import com.delivery.model.DriverStatus;
import com.delivery.model.Item;
import com.delivery.service.DeliveryManager;
import com.delivery.service.EmailNotificationService;
import com.delivery.service.OrderManager;
import com.delivery.service.UserManager;

public class Driver {

    private DeliveryManager deliveryManager = new DeliveryManager();

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.drive();
    }

    private void drive() {
        deliveryManager.onboardCustomer("tom", "aa@gmail.com", "123456789", "address1");
        deliveryManager.onboardCustomer("paul", "bb@gmail.com", "987654321", "address2");

        deliveryManager.onboardDriver("driver1", "1234556", "driver1@gmail.com");
        deliveryManager.onboardDriver("driver2", "1234556", "driver2@gmail.com");

        Item phone = new Item("1", "iPhone");
        Item bag = new Item("2", "Bag");
        Item shoe = new Item("3", "Shoe");

        Customer tom = deliveryManager.getUserManager().getCustomers().get(0);
        Customer paul = deliveryManager.getUserManager().getCustomers().get(1);

        deliveryManager.placeOrder(tom, paul, phone);
        deliveryManager.placeOrder(tom, paul, bag);
        deliveryManager.placeOrder(paul, tom, shoe);

        deliveryManager.orderPickup("driver1", "1");
        deliveryManager.orderDelivered("driver1", "1");

        deliveryManager.orderStatus("1");
        


//
//
//        Customer tom = deliveryManager.getUserManager().getCustomers().get(0);
//        Customer paul = deliveryManager.getUserManager().getCustomers().get(1);
//
//        deliveryManager.placeOrder(tom, paul, new Item("1", "Bag"));
//        deliveryManager.driverStatus("driver1");
//
//        deliveryManager.orderPickup("driver1", "1");
//        deliveryManager.orderStatus("1");
//
//        deliveryManager.placeOrder(paul, tom, new Item("2", "Bat"));
//        deliveryManager.onboardDriver("driver2", "1234556", "driver2@gmail.com");
//        deliveryManager.orderDelivered("driver1", "1");
//        deliveryManager.driverStatus("driver2");
//
//        deliveryManager.orderStatus("2");

    }

}
