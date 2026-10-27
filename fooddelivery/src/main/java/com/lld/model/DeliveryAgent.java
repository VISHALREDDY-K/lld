package com.lld.model;

public class DeliveryAgent extends User {
    private Address currentLocation;
    private boolean isAvailable;
    

    public DeliveryAgent(String name, String phone, Address currentLocation) {
        super(name, phone);
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
