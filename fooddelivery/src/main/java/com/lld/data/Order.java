package com.lld.data;

import java.util.ArrayList;
import java.util.List;

import com.lld.enums.OrderStatus;

public class Order {
    private final String id;
    private Customer customer;
    private Restaurant restaurant;
    private DeliveryAgent deliveryAgent;
    private List<OrderItem> items;
    private OrderStatus status;
    private final List<OrderObserver> observers;
    
    public Order(String id, Customer customer, Restaurant restaurant, List<OrderItem> items) {
        this.id = id;
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.observers = new ArrayList<>();
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public DeliveryAgent getDeliveryAgent() { return deliveryAgent; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public List<OrderObserver> getObservers() { return observers; }

    
}
