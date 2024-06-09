package com.example.kalifornia;

public class ShopModel {

    private String name;
    private String price;

    public ShopModel(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
