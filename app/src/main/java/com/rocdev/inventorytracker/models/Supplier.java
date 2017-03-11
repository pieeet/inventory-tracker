package com.rocdev.inventorytracker.models;

/**
 * Created by piet on 11-03-17.
 */

public class Supplier {
    long id;
    String name;

    public Supplier(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
