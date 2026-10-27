package com.lld.model;

import java.util.UUID;

import com.lld.observer.OrderObserver;

public class User implements OrderObserver {
    private final String id;
    private final String name;
    private final String phone;

    public User(String name, String phone) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.phone = phone;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    @Override
    public void onUpdate(Order order) {
        System.out.println("User " + name + " notified of order status: " + order.getStatus());
    }
}
