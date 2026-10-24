package com.lld.model;

import com.lld.enums.VehicleSize;

public class Car extends Vehicle {
    public Car(String numberPlate) {
        super(numberPlate, VehicleSize.MEDIUM);
    }
}
