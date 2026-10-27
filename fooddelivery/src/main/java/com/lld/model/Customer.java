package com.lld.model;

import java.util.ArrayList;
import java.util.List;

public class Customer extends User {
    private Address preferredAddress;
    private List<Order> orders;

    public Customer(String name, String phone, Address address) {
        super(name, phone);
        this.preferredAddress = address;
        this.orders = new ArrayList<>();
    }

    @Override
    public void onUpdate(Order order) {
        System.out.println("Customer " + this.getId() + " notified of order status: " + order.getStatus());
    }

    public Address getAddress() { return preferredAddress; }
    public Address getPreferredAddress() { return preferredAddress; }
    public List<Order> getOrders() { return orders; }
    public void addOrder(Order order){
        orders.add(order);
    }
}
