package com.lld.data;

public class User implements OrderObserver {
    private final String id;
    private final String name;
    private final String phone;

    public User(String id, String name, String phone) {
        this.id = id;
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
