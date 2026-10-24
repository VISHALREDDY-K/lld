package com.lld.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lld.enums.VehicleSize;

public class ParkingFloor {
    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, Map<VehicleSize, Integer> capacityMap) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();

        for (Map.Entry<VehicleSize, Integer> entry : capacityMap.entrySet()) {
            VehicleSize size = entry.getKey();
            int count = entry.getValue();
            for (int i = 1; i <= count; i++) {
                String spotId = "F" + floorNumber + "-" + size.name() + i;
                spots.add(new ParkingSpot(spotId, size));
            }
        }
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }
}
