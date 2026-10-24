package com.lld.core;

import com.lld.enums.VehicleSize;
import com.lld.exceptions.ParkingException;
import com.lld.model.Vehicle;

public class ParkingSpot {
    private final String id;
    private final VehicleSize vehicleSize;
    private Vehicle vehicle;

    public ParkingSpot(String id, VehicleSize vehicleSize) {
        this.id = id;
        this.vehicleSize = vehicleSize;
        this.vehicle = null;
    }

    public boolean canPark(Vehicle vehicle) {
        if (this.vehicle != null) {
            return false;
        }
        return vehicle.getSize().ordinal() <= this.vehicleSize.ordinal();
    }

    public synchronized void park(Vehicle vehicle) {
        if (canPark(vehicle)) {
            this.vehicle = vehicle;
            return;
        }
        throw new ParkingException("Spot is already occupied");
    }

    public synchronized Vehicle unpark() {
        if (this.vehicle == null) {
            throw new ParkingException("Spot is empty");
        }
        Vehicle vehicle = this.vehicle;
        this.vehicle = null;
        return vehicle;
    }

    public String getId() {
        return id;
    }

    public VehicleSize getVehicleSize() {
        return vehicleSize;
    }

    public VehicleSize getSpotSize() {
        return vehicleSize;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
