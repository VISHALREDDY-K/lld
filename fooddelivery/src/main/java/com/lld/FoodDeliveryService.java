package com.lld;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

import com.lld.enums.OrderStatus;
import com.lld.model.Address;
import com.lld.model.Customer;
import com.lld.model.DeliveryAgent;
import com.lld.model.Menu;
import com.lld.model.Order;
import com.lld.model.OrderItem;
import com.lld.model.Restaurant;
import com.lld.strategy.RestaurantSearchStrategy;

public class FoodDeliveryService {
    private static volatile FoodDeliveryService INSTANCE;
    private final Map<String, Customer> users = new ConcurrentHashMap<>();
    private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>();
    private final Map<String, DeliveryAgent> deliveryAgents = new ConcurrentHashMap<>();
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    private FoodDeliveryService() {}

    public static FoodDeliveryService getInstance() {
        if (INSTANCE == null) {
            synchronized (FoodDeliveryService.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FoodDeliveryService();
                }
            }
        }
        return INSTANCE;
    }

    public Customer registerUser(String name, String phone, Address address){
        Customer customer = new Customer(name, phone, address);
        users.put(customer.getId(), customer);
        return customer;
    }

    public Restaurant registerRestaurant(String name, Menu menu, Address address){
        Restaurant restaurant = new Restaurant(name, menu, address);
        restaurants.put(restaurant.getId(), restaurant);
        return restaurant;
    }

    public DeliveryAgent registerDeliveryPerson(String name, String phone, Address address){
        DeliveryAgent deliveryAgent = new DeliveryAgent(name, phone, address);
        deliveryAgents.put(deliveryAgent.getId(), deliveryAgent);
        return deliveryAgent;
    }

    public Order placeOrder(String customerId, String restaurantId, List<OrderItem> items){
        if(!users.containsKey(customerId) || !restaurants.containsKey(restaurantId)){
            throw new NoSuchElementException("Customer or Restaurant not found");
        }
        if(items==null || items.size()==0){
            throw new IllegalArgumentException("Items list cannot be empty");
        }
        for(OrderItem item : items){
            if(item.getQuantity()<=0){
                throw new IllegalArgumentException("Item quantity must be positive");
            }
            if(!item.getMenuItem().isAvailable()){
                throw new IllegalArgumentException("Item "+item.getMenuItem().getName()+" is not available");
            }   
        }

        Customer customer = users.get(customerId);
        Restaurant restaurant = restaurants.get(restaurantId);
        
        Order order = new Order(customer, restaurant, items);
        customer.addOrder(order);
        orders.put(order.getId(), order);
        order.setStatus(OrderStatus.PENDING);

        return order;
    }

    public void cancelOrder(String orderId){
        if(orders.containsKey(orderId)){
            Order order = orders.get(orderId);
            if(!order.cancel()){
                System.err.println("Order cannot be cancelled");
            }
            orders.remove(orderId);
        }
        else{
            throw new NoSuchElementException("Order not found");
        }
    }

    public void assignDeliveryAgent(String orderId, String agentId){
        if(orders.containsKey(orderId) && deliveryAgents.containsKey(agentId)){
            Order order = orders.get(orderId);
            DeliveryAgent agent = deliveryAgents.get(agentId);
            if(order.getStatus() == OrderStatus.READY_FOR_PICKUP){
                order.setDeliveryAgent(agent);
                order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
                agent.setAvailable(false);
            }
        }
        else{
            throw new NoSuchElementException("Order or DeliveryAgent not found");
        }
    }

    public List<Restaurant> searcRestaurants(RestaurantSearchStrategy strategy){
        return strategy.search(new ArrayList<Restaurant>(restaurants.values()));
    }
}
