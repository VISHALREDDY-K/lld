package com.lld.model;

import com.lld.enums.VehicleSize;

public abstract class Vehicle {
    private final String numberPlate;
    private final VehicleSize size;

    public Vehicle(String numberPlate, VehicleSize size) {
        this.numberPlate = numberPlate;
        this.size = size;
    }

    public String getNumberPlate() { return numberPlate; }
    public VehicleSize getSize() { return size; }

    @Override
    public String toString() {
        return size + "[" + numberPlate + "]";
    }
}
