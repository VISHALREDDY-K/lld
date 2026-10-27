package com.lld.data;

import java.util.ArrayList;

public class DeliveryAgent extends User {
    private Address currentLocation;
    private boolean isAvailable;
    

    public DeliveryAgent(String id, String name, String phone, Address currentLocation) {
        super(id, name, phone);
        this.currentLocation = currentLocation;
        this.isAvailable = true;
    }

    @Override
    public void onUpdate(Order order) {
        System.out.println("Delivery Agent" + this.getId() + "updated about order" + order.getId() + "which is now " + order.getStatus());
    }

    public Address getCurrentLocation() { return currentLocation; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable; }
}
