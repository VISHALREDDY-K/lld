package com.lld.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lld.enums.OrderStatus;
import com.lld.observer.OrderObserver;

public class Order {
    private final String id;
    private Customer customer;
    private Restaurant restaurant;
    private DeliveryAgent deliveryAgent;
    private List<OrderItem> items;
    private OrderStatus status;
    private final List<OrderObserver> observers;
    
    public Order(Customer customer, Restaurant restaurant, List<OrderItem> items) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = items;
        this.status = OrderStatus.PENDING;
        this.observers = new ArrayList<>();
        this.observers.add(customer);
        this.observers.add(restaurant);        
    }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public Restaurant getRestaurant() { return restaurant; }
    public DeliveryAgent getDeliveryAgent() { return deliveryAgent; }
    public List<OrderItem> getItems() { return items; }
    public OrderStatus getStatus() { return status; }
    public List<OrderObserver> getObservers() { return observers; }
    public void addObserver(OrderObserver observer) { observers.add(observer); }
    public void removeObserver(OrderObserver observer) { observers.remove(observer); }
    public void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.onUpdate(this);
        }
    }

    public void setStatus(OrderStatus status) {
        if(this.status!=status){
            this.status = status;
            if((this.status == OrderStatus.DELIVERED || this.status == OrderStatus.CANCELLED) && this.deliveryAgent != null){
                this.deliveryAgent.setAvailable(true);
            }
            notifyObservers();
        }
    }

    public boolean cancel(){
        if(this.status == OrderStatus.PENDING || this.status == OrderStatus.CONFIRMED){
            this.status = OrderStatus.CANCELLED;
            if(this.deliveryAgent != null){
                this.deliveryAgent.setAvailable(true);
            }
            notifyObservers();
            return true;
        }
        return false;
    }

    public void setDeliveryAgent(DeliveryAgent deliveryAgent) {
        if(this.status == OrderStatus.READY_FOR_PICKUP){
            this.deliveryAgent = deliveryAgent;
            this.deliveryAgent.setAvailable(false);
            notifyObservers();
        }  
    }
}
