package com.lld.strategy;

import com.lld.model.ParkingTicket;

public class HourlyFeeStrategy implements FeeStrategy {
    private final double feePerHour;

    public HourlyFeeStrategy(double feePerHour) {
        this.feePerHour = feePerHour;
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        long duration = ticket.getDurationInHours();
        if (duration <= 0) {
            duration = 1;
        }
        return duration * feePerHour;
    }
}
