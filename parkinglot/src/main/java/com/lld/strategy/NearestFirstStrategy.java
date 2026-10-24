package com.lld.strategy;

import java.util.List;

import com.lld.core.ParkingFloor;
import com.lld.core.ParkingSpot;
import com.lld.model.Vehicle;

public class NearestFirstStrategy implements SpotAllocationStrategy {

    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot bestFitOnFloor = null;

            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.canPark(vehicle)) {
                    if (bestFitOnFloor == null ||
                            spot.getSpotSize().ordinal() < bestFitOnFloor.getSpotSize().ordinal()) {
                        bestFitOnFloor = spot;
                        if (bestFitOnFloor.getSpotSize() == vehicle.getSize()) {
                            break;
                        }
                    }
                }
            }

            if (bestFitOnFloor != null) {
                return bestFitOnFloor; // Found on nearest floor, stop searching higher floors
            }
        }
        return null;
    }
}
