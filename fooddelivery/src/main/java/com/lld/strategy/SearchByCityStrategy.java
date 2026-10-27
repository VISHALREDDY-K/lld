package com.lld.strategy;

import com.lld.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class SearchByCityStrategy implements RestaurantSearchStrategy {
    private final String city;

    public SearchByCityStrategy(String city) {
        this.city = city;
    }

    @Override
    public List<Restaurant> search(List<Restaurant> restaurants) {
        List<Restaurant> matchedRestaurants = new ArrayList<>();
        if (restaurants == null || city == null) {
            return matchedRestaurants;
        }

        for (Restaurant restaurant : restaurants) {
            if (restaurant != null && restaurant.getLocation() != null 
                    && city.equalsIgnoreCase(restaurant.getLocation().getCity())) {
                matchedRestaurants.add(restaurant);
            }
        }

        return matchedRestaurants;
    }

    public String getCity() {
        return city;
    }
}
