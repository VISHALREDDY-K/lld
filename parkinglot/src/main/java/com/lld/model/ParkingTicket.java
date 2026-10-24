package com.lld.model;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import com.lld.core.ParkingSpot;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final Instant entryTime;
    private Instant exitTime;

    public ParkingTicket(Vehicle vehicle, ParkingSpot parkingSpot) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.entryTime = Instant.now();
        this.exitTime = null;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public Instant getEntryTime() {
        return entryTime;
    }

    public Instant getExitTime() {
        return exitTime;
    }

    public void setExitTime(Instant exitTime) {
        this.exitTime = exitTime;
    }

    public long getDurationInHours() {
        Instant end = (this.exitTime != null) ? this.exitTime : Instant.now();
        long hours = Duration.between(entryTime, end).toHours();
        return Math.max(1, hours);
    }

    @Override
    public String toString() {
        return "Ticket[" + ticketId.substring(0, 8) + "] "
                + vehicle + " -> Spot " + parkingSpot.getId();
    }
}
