package com.lld.model;

import com.lld.enums.VehicleSize;

public class Truck extends Vehicle {
    public Truck(String numberPlate) {
        super(numberPlate, VehicleSize.LARGE);
    }
}
