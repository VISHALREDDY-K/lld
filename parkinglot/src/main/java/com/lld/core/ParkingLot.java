package com.lld.core;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.exceptions.ParkingException;
import com.lld.model.ParkingTicket;
import com.lld.model.Vehicle;
import com.lld.strategy.FeeStrategy;
import com.lld.strategy.SpotAllocationStrategy;

public class ParkingLot {
    private static volatile ParkingLot instance;
    private SpotAllocationStrategy strategy;
    private List<ParkingFloor> floors;
    private FeeStrategy feeStrategy;
    private final Map<String, ParkingTicket> tickets;

    private ParkingLot() {
        this.tickets = new ConcurrentHashMap<>();
    }

    public void initialize(SpotAllocationStrategy strategy, FeeStrategy feeStrategy, List<ParkingFloor> floors) {
        this.strategy = strategy;
        this.feeStrategy = feeStrategy;
        this.floors = floors;
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot();
                }
            }
        }
        return instance;
    }

    public void resetInstance() {
        synchronized (ParkingLot.class) {
            instance = null;
        }
    }

    public void setStrategy(SpotAllocationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setFeeStrategy(FeeStrategy feeStrategy) {
        this.feeStrategy = feeStrategy;
    }

    public void setFloors(List<ParkingFloor> floors) {
        this.floors = floors;
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        while (true) {
            ParkingSpot spot = strategy.findSpot(floors, vehicle);
            if (spot == null) {
                throw new ParkingException("No spot available");
            }
            try {
                spot.park(vehicle);
            } catch (ParkingException e) {
                continue;
            }
            ParkingTicket ticket = new ParkingTicket(vehicle, spot);
            tickets.put(ticket.getTicketId(), ticket);
            return ticket;
        }
    }

    public double unparkVehicle(String ticketId) {
        ParkingTicket ticket = tickets.get(ticketId);
        if (ticket == null) {
            throw new ParkingException("Ticket not found");
        }
        ticket.getParkingSpot().unpark();
        tickets.remove(ticketId);
        return feeStrategy.calculateFee(ticket);
    }
}
