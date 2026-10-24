package com.lld.strategy;

import com.lld.model.ParkingTicket;

public interface FeeStrategy {
    double calculateFee(ParkingTicket ticket);
}
