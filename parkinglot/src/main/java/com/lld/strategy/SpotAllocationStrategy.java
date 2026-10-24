package com.lld.strategy;

import java.util.List;

import com.lld.core.ParkingFloor;
import com.lld.core.ParkingSpot;
import com.lld.model.Vehicle;

public interface SpotAllocationStrategy {
    ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle);
}