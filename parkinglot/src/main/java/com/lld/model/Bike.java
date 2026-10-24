package com.lld.model;

import com.lld.enums.VehicleSize;

public class Bike extends Vehicle {
    public Bike(String numberPlate) {
        super(numberPlate, VehicleSize.SMALL);
    }
}
