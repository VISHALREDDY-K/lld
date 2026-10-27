package com.lld.strategy;

import com.lld.model.MenuItem;
import com.lld.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class SearchByMenuStrategy implements RestaurantSearchStrategy {
    private final String itemName;

    public SearchByMenuStrategy(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public List<Restaurant> search(List<Restaurant> restaurants) {
        List<Restaurant> matchedRestaurants = new ArrayList<>();
        if (restaurants == null || itemName == null) {
            return matchedRestaurants;
        }

        // Loop 1 (Outer): Iterate through each restaurant
        for (Restaurant restaurant : restaurants) {
            if (restaurant == null || restaurant.getMenu() == null || restaurant.getMenu().getItems() == null) {
                continue;
            }

            // Loop 2 (Inner): Iterate through each menu item in the restaurant's menu
            for (MenuItem item : restaurant.getMenu().getItems().values()) {
                if (item != null && item.getName() != null && item.getName().equalsIgnoreCase(itemName)) {
                    matchedRestaurants.add(restaurant);
                    break; // Break early to avoid duplicate restaurant entries
                }
            }
        }

        return matchedRestaurants;
    }

    public String getItemName() {
        return itemName;
    }
}
