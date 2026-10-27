package com.lld.model;

import java.util.UUID;

import com.lld.observer.OrderObserver;

public class Restaurant implements OrderObserver {
    private final String id;
    private final String name;
    private final Menu menu;
    private Address location;
    

    public Restaurant(String name, Menu menu, Address location) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.menu = menu;
        this.location = location;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Menu getMenu() { return menu; }
    public Address getAddress() { return location; }
    public Address getLocation() { return location; }

    @Override
    public void onUpdate(Order order) {
        System.out.println("Restaurant " + name + " notified of order status: " + order.getStatus());
    }
}
