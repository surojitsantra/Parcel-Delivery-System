package com.delivery.model;

import lombok.Data;

@Data
public class Driver extends User {
    private DriverStatus status;
}
