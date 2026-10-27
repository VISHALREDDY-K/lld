package com.lld.strategy;

import com.lld.model.Restaurant;
import java.util.List;

public interface RestaurantSearchStrategy {
    List<Restaurant> search(List<Restaurant> restaurants);
}
