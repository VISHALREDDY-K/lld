package com.lld;

import java.util.List;
import java.util.Map;

import com.lld.core.ParkingFloor;
import com.lld.core.ParkingLot;
import com.lld.enums.VehicleSize;
import com.lld.exceptions.ParkingException;
import com.lld.model.Bike;
import com.lld.model.Car;
import com.lld.model.ParkingTicket;
import com.lld.model.Truck;
import com.lld.model.Vehicle;
import com.lld.strategy.HourlyFeeStrategy;
import com.lld.strategy.NearestFirstStrategy;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Parking Lot Demo ===\n");

        // Setup: 2 floors
        // Floor 1: 2 SMALL, 3 MEDIUM, 1 LARGE
        // Floor 2: 2 SMALL, 2 MEDIUM
        ParkingFloor floor1 = new ParkingFloor(1, Map.of(
                VehicleSize.SMALL,  2,
                VehicleSize.MEDIUM, 3,
                VehicleSize.LARGE,  1
        ));
        ParkingFloor floor2 = new ParkingFloor(2, Map.of(
                VehicleSize.SMALL,  2,
                VehicleSize.MEDIUM, 2
        ));

        ParkingLot lot = ParkingLot.getInstance();
        lot.initialize(
                new NearestFirstStrategy(),
                new HourlyFeeStrategy(50.0),  // ₹50/hour
                List.of(floor1, floor2)
        );

        // Park vehicles
        Vehicle bike1  = new Bike("BIKE-001");
        Vehicle car1   = new Car("CAR-001");
        Vehicle car2   = new Car("CAR-002");
        Vehicle truck1 = new Truck("TRK-001");
        Vehicle car3   = new Car("CAR-003");

        ParkingTicket t1 = lot.parkVehicle(bike1);
        System.out.println("Parked: " + t1);

        ParkingTicket t2 = lot.parkVehicle(car1);
        System.out.println("Parked: " + t2);

        ParkingTicket t3 = lot.parkVehicle(car2);
        System.out.println("Parked: " + t3);

        ParkingTicket t4 = lot.parkVehicle(truck1);
        System.out.println("Parked: " + t4);

        ParkingTicket t5 = lot.parkVehicle(car3);
        System.out.println("Parked: " + t5);

        // Unpark car1 and calculate fee
        System.out.println("\n--- Unparking CAR-001 ---");
        double fee = lot.unparkVehicle(t2.getTicketId());
        System.out.printf("Fee for %s: ₹%.2f (%.0f hour minimum)%n", car1, fee, (double)t2.getDurationInHours());

        // Try to park another car — should find the freed spot
        ParkingTicket t6 = lot.parkVehicle(new Car("CAR-004"));
        System.out.println("Parked after unpark: " + t6);

        // Test: no spot available
        System.out.println("\n--- Testing full lot ---");
        try {
            lot.parkVehicle(new Truck("TRK-002")); // no LARGE spot left
        } catch (ParkingException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Test: invalid ticket
        System.out.println("\n--- Testing invalid ticket ---");
        try {
            lot.unparkVehicle("fake-ticket-id");
        } catch (ParkingException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n=== Demo Complete ===");
    }
}