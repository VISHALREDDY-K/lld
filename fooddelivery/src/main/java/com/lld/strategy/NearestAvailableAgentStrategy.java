package com.lld.strategy;

import java.util.List;
import java.util.Optional;

import com.lld.model.Address;
import com.lld.model.DeliveryAgent;
import com.lld.model.Order;

public class NearestAvailableAgentStrategy implements DeliveryAssignmentStrategy{

    @Override
    public Optional<DeliveryAgent> findAgent(List<DeliveryAgent> agents, Order order) {
        Address restaurantAddress = order.getRestaurant().getAddress();
        Address customerAddress = order.getCustomer().getAddress();

        DeliveryAgent nearestAvailable = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for(DeliveryAgent agent : agents){
            if(agent.isAvailable()){
                Address deliveryAgentLocation = agent.getCurrentLocation();
                double dist = restaurantAddress.getDistanceTo(deliveryAgentLocation) +
                            deliveryAgentLocation.getDistanceTo(customerAddress);
                
                if(dist < nearestDistance){
                    nearestDistance = dist;
                    nearestAvailable = agent;
                }
            }
        }
        return Optional.ofNullable(nearestAvailable);
        
    }
    
}
