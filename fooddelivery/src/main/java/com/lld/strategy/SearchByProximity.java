package com.lld.strategy;

import java.util.List;

import com.lld.model.Address;
import com.lld.model.Restaurant;

public class SearchByProximity implements RestaurantSearchStrategy {
    
    private final Address userAddress;
    
    public SearchByProximity(Address userAddress) {
        this.userAddress = userAddress;
    }
    
    private int comparator(Restaurant r1, Restaurant r2) {
        return Double.compare(r1.getAddress().getDistanceTo(userAddress), r2.getAddress().getDistanceTo(userAddress));
    }

    @Override
    public List<Restaurant> search(List<Restaurant> restaurants) {
        restaurants.sort(this::comparator);
        return restaurants;
    }
    
}
