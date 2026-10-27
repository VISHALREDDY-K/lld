package com.lld.data;

public class Restaurant {
    private final String id;
    private final String name;
    private final Menu menu;
    private Address location;
    

    public Restaurant(String id, String name, Menu menu, Address location) {
        this.id = id;
        this.name = name;
        this.menu = menu;
        this.location = location;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Menu getMenu() { return menu; }
    public Address getLocation() { return location; }
}
