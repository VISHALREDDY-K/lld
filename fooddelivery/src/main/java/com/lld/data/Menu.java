package com.lld.data;

import java.util.Map;

public class Menu {
    private final String id;
    private final Map<String, MenuItem> items;
    

    public Menu(String id, Map<String, MenuItem> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() { return id; }
    public Map<String, MenuItem> getItems() { return items; }

    public MenuItem getItem(String id) {
        return items.get(id);
    }
}
