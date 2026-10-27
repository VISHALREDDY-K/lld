package com.lld.data;

public class OrderItem {
    private final String id;
    private final MenuItem menuItem;
    private final int quantity;
    
    public OrderItem(String id, MenuItem menuItem, int quantity) {
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public MenuItem getMenuItem() { return menuItem; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return menuItem.getPrice(); }

    public double getTotalPrice() {
        return quantity * menuItem.getPrice();
    }
}
