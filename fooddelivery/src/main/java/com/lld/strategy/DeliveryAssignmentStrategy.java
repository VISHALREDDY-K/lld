package com.lld.strategy;

import java.util.List;
import java.util.Optional;

import com.lld.model.DeliveryAgent;
import com.lld.model.Order;

public interface DeliveryAssignmentStrategy {
    Optional<DeliveryAgent> findAgent(List<DeliveryAgent> agents, Order order);
}
